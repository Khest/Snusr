package no.hbv.gruppe1.snusr.snusr.dataclasses;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Dakh on 2016-05-28.
 */
public class BluetoothHandler2 {
    private ConnectThread connectThread;
    private ConnectedThread connectedThread;
    private AcceptThread acceptThread;
    private final BluetoothAdapter mBluetoothAdapter;
    private final Handler mHandler;
    private int status;

    public static final int STATE_NONE          = 1;
    public static final int STATE_WAITING       = 2;
    public static final int STATE_CONNECTING    = 3;
    public static final int STATE_CONNECTED     = 4;

    public BluetoothHandler2(BluetoothAdapter mBluetoothAdapter, Handler mHandler) {
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.mHandler = mHandler;
        this.status = STATE_NONE;
    }

    public void connectAsClient(final BluetoothDevice device) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connectThread = new ConnectThread(device);
            }
        }).start();
    }

    public void connectAsServer(final BluetoothDevice device) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                acceptThread = new AcceptThread();
            }
        }).start();
    }

    public synchronized void start() {
        closeConnectThread();
        closeAcceptThread();
        acceptThread = new AcceptThread();
        acceptThread.start();
    }

    public synchronized void connect(BluetoothDevice device) {
        closeConnectThread();
        connectThread = new ConnectThread(device);
        connectThread.start();
    }

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device) { //, final String socketType
        closeConnectThread();
        closeAcceptThread();
        connectedThread = new ConnectedThread(socket);
        connectedThread.start();
        Message msg = mHandler.obtainMessage(Globals.DEVICE_NAME_IDENTIFIER, device.getName());
        Bundle bundle = new Bundle();
        bundle.putString("0", device.getName());
        msg.setData(bundle);
        mHandler.sendMessage(msg);
    }



    public synchronized void stop() {
        closeConnectThread();
        closeConnectedThread();
        closeAcceptThread();
    }

    public void write(byte[] out) {
        ConnectedThread c;
        synchronized (this) {
            if (!connectedThread.isAlive()) return;
            c = connectedThread;
        }
        c.write(out);
    }

    public synchronized int getStatus() {
        return this.status;
    }

    private synchronized void setStatus(int status) {
        this.status = status;
        mHandler.obtainMessage(Globals.MESSAGE_STATE_CHANGE, status, -1).sendToTarget();
    }

    private void closeConnectThread() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
        }
    }

    private void closeConnectedThread() {
        if (connectedThread != null) {
            connectedThread.cancel();
            connectedThread = null;
        }
    }

    private void closeAcceptThread() {
        if (acceptThread != null) {
            acceptThread.cancel();
            acceptThread = null;
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket socket = null;
            mmDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                //socket = device.createRfcommSocketToServiceRecord(Globals.MY_UUID);

                socket =(BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
                //
            } catch (Exception e) {
                Log.e(Globals.TAG, " Error creating socket"); }
            mmSocket = socket;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                    Log.e(Globals.TAG, " Error attempting to close bluetooth connection " + closeException.getMessage());
                }
                return;
            }
            synchronized (BluetoothHandler2.this) {
                connectThread = null;
            }

            connected(mmSocket, mmDevice);

            // Do work to manage the connection (in a separate thread)
            //sendData(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.i(Globals.TAG, " Connection to bluetooth device closed");
            }
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(Globals.APP_NAME, Globals.MY_UUID);
            } catch (IOException e) {
                Log.e(Globals.TAG, " Bluetooth server error on connect: " + e.getMessage());
            }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket;
            // Keep listening until exception occurs or a socket is returned
            while (true) { //TODO Change to listen to connected state
                try {
                    socket = mmServerSocket.accept();
                // If a connection was accepted
                if (socket != null) {
                    //receiveData(socket);
                    //mmServerSocket.close();
                    synchronized (BluetoothHandler2.this) {
                        connected(socket, socket.getRemoteDevice());
                    }
                    break;
                }
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.i(Globals.TAG, " Closed bluetooth server");
            }
        }
    }

    /**
     * This thread runs during a connection with a remote device.
     * It handles all incoming and outgoing transmissions.
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            //Log.d(Globals.TAG, "create ConnectedThread: " + socketType);
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the BluetoothSocket input and output streams
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(Globals.TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(Globals.TAG, "BEGIN mConnectedThread");
            byte[] buffer = new byte[1024];
            int bytes;

            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);

                    // Send the obtained bytes to the UI Activity
                   mHandler.obtainMessage(Globals.MESSAGE_READ, bytes, -1, buffer) //TODO Handle incoming data properly
                           .sendToTarget();
                } catch (IOException e) {
                    Log.e(Globals.TAG, "disconnected", e);
                    //connectionLost(); //// FIXME: 2016-05-28
                    // Start the service over to restart listening mode
                   // BluetoothChatService.this.start(); //// FIXME: 2016-05-28
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         *
         * @param buffer The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                mmOutStream.write(buffer);
                // Share the sent message back to the UI Activity
                mHandler.obtainMessage(Globals.MESSAGE_WRITE, -1, -1, buffer) //TODO Handle outgoing data properly
                        .sendToTarget();
            } catch (IOException e) {
                Log.e(Globals.TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(Globals.TAG, "close() of connect socket failed", e);
            }
        }
    }

}
