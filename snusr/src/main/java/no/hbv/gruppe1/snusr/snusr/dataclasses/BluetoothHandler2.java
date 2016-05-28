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
    ConnectThread connectThread;
    ConnectedThread connectedThread;
    AcceptThread acceptThread;
    BluetoothAdapter mBluetoothAdapter;
    Handler mHandler;

    public BluetoothHandler2(BluetoothAdapter mBluetoothAdapter, Handler mHandler) {
        this.mBluetoothAdapter = mBluetoothAdapter;
        this.mHandler = mHandler;
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

    private void sendData(BluetoothSocket socket) {

    }

    private void receiveData(BluetoothSocket socket) {
        try {
            InputStream is = socket.getInputStream();

        } catch (IOException ex) {
            Log.e(Globals.TAG, ex.getMessage());
        }
    }

    public synchronized void start(BluetoothDevice device) {
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

    public synchronized void connected(BluetoothSocket socket, BluetoothDevice device, final String socketType) {
        closeConnectThread();
        closeAcceptThread();
        connectedThread = new ConnectedThread(socket, socketType);
        connectedThread.start();
        //Message msg = mHandler.obtainMessage(device.getName()); //// FIXME: 2016-05-28 fix Constant
//        Bundle bundle = new Bundle();
//        bundle.putString("0", device.getName());
//        msg.setData(bundle);
//        mHandler.sendMessage(msg);

    }

    private void closeConnectThread() {
        if (connectThread != null) {
            connectThread.cancel();
            connectThread = null;
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
                } catch (IOException closeException) { }
                return;
            }

            // Do work to manage the connection (in a separate thread)
            sendData(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private class AcceptThread extends Thread {
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;
            try {
                // MY_UUID is the app's UUID string, also used by the client code
                tmp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(Globals.APP_NAME, Globals.MY_UUID);
            } catch (IOException e) { }
            mmServerSocket = tmp;
        }

        public void run() {
            BluetoothSocket socket;
            // Keep listening until exception occurs or a socket is returned
            while (true) {
                try {
                    socket = mmServerSocket.accept();
                // If a connection was accepted
                if (socket != null) {
                    // Do work to manage the connection (in a separate thread)
                    receiveData(socket);
                    mmServerSocket.close();
                    break;
                }
                } catch (IOException e) {
                    break;
                }
            }
        }

        /** Will cancel the listening socket, and cause the thread to finish */
        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) { }
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

        public ConnectedThread(BluetoothSocket socket, String socketType) {
            Log.d(Globals.TAG, "create ConnectedThread: " + socketType);
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
                   // mHandler.obtainMessage(SyncStateContract.Constants.MESSAGE_READ, bytes, -1, buffer) //// FIXME: 2016-05-28
                   //         .sendToTarget();
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
                //mHandler.obtainMessage(Constants.MESSAGE_WRITE, -1, -1, buffer) //// FIXME: 2016-05-28
                //        .sendToTarget();
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
