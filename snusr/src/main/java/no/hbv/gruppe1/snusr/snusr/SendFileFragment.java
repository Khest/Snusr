package no.hbv.gruppe1.snusr.snusr;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import no.hbv.gruppe1.snusr.snusr.dataclasses.BluetoothHandler2;
import no.hbv.gruppe1.snusr.snusr.dataclasses.Globals;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Long Huynh on 27.05.2016.
 */
@SuppressWarnings("Duplicates")
public class SendFileFragment extends Fragment {
    private ListView listDevicesView;
    private List<BluetoothDevice> listDevices;
    Button btn_send, btn_search;
    BluetoothHandler2 bluetoothHandler = null;
    BluetoothAdapter mBluetoothAdapter = null;
    ArrayAdapter<String> adapter;


    //UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final int REQUEST_CONNECTION = 1;
    private final int REQUEST_ENABLE_BT = 2;


    // Create a BroadcastReceiver for ACTION_FOUND
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                adapter.add(device.getName() + "\n" + device.getAddress());
                listDevices.add(device);
            }
        }
    };

    private final Handler messageHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Activity fragmentActivity = getActivity();
            switch (msg.what) {
                case Globals.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothHandler2.STATE_CONNECTED:
                            Log.i(Globals.TAG, " BT: Connected");
                            //do something
                            break;
                        case BluetoothHandler2.STATE_CONNECTING:
                            Log.i(Globals.TAG, " BT: Connecting");
                            //do something
                            break;
                        case BluetoothHandler2.STATE_NONE:
                            Log.i(Globals.TAG, " BT: None");
                            //do something
                            break;
                        case BluetoothHandler2.STATE_WAITING:
                            Log.i(Globals.TAG, " BT: Waiting");
                            //do something
                            break;
                    }
                    break;
                case Globals.DEVICE_NAME_IDENTIFIER:
                    if (fragmentActivity != null) {
                        Toast.makeText(fragmentActivity,
                                "Connected to " + msg.getData().getString(Globals.DEVICE_NAME),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Globals.MESSAGE_READ:
                    byte[] buffer = (byte[]) msg.obj;
                    String incoming = new String(buffer, 0, msg.arg1);
                    Toast.makeText(fragmentActivity, "Incoming: " + incoming, Toast.LENGTH_SHORT).show();
                    break;
                case Globals.MESSAGE_WRITE:
                    byte[] buffer2 = (byte[]) msg.obj;
                    String outgoing = new String(buffer2);
                    Toast.makeText(fragmentActivity, "Outgoing: " + outgoing, Toast.LENGTH_SHORT).show();
                    break;
                case Globals.MESSAGE_TOAST:
                    if (fragmentActivity != null) {
                        Toast.makeText(fragmentActivity, msg.getData().getString(Globals.TOAST), Toast.LENGTH_SHORT).show();
                    }
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else if (bluetoothHandler == null) {
            setUp();
            //todo
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bluetoothHandler != null) {
            bluetoothHandler.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bluetoothHandler != null) {
            if (bluetoothHandler.getStatus() == BluetoothHandler2.STATE_NONE) {
                bluetoothHandler.start();
            }
        }
    }

    private void connectDevice(String address) {
        bluetoothHandler.connectAsClient(mBluetoothAdapter.getRemoteDevice(address));
    }

    private void connectDevice(Intent data) {
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        bluetoothHandler.connect(mBluetoothAdapter.getRemoteDevice(address));
    }

    private void makeDeviceDiscoverable() {
        if (mBluetoothAdapter.getScanMode() != BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 360);
            startActivity(discoverableIntent);
        }
    }

    private void setUp() {
        this.listDevices = new ArrayList<>();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1);
        listDevicesView.setAdapter(adapter);

        this.bluetoothHandler = new BluetoothHandler2(mBluetoothAdapter, messageHandler);

        listDevicesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(Globals.TAG, " " + adapter.getItem(position));
                //connectDevice(listDevices.get(position).getAddress());
                Intent enableIntent = new Intent(listDevices.get(position).getAddress());
                startActivityForResult(enableIntent, REQUEST_CONNECTION);


               // Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                //startActivityForResult(serverIntent, REQUEST_CONNECTION);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
                //startActivityForResult(serverIntent, REQUEST_CONNECTION);
                bluetoothHandler.write(" dette er en test! ".getBytes());
            }
        });


        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                getActivity().registerReceiver(mReceiver, filter);
                mBluetoothAdapter.startDiscovery();
                makeDeviceDiscoverable();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    setUp();
                } else {
                    Toast.makeText(getActivity(), "BT not enabled", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CONNECTION:
                if (resultCode == Activity.RESULT_OK) {
                    connectDevice(data);
                }
        }
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         return inflater.inflate(R.layout.fragment_send, container, false);
     }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listDevicesView = (ListView) view.findViewById(R.id.list_devices);
        btn_search = (Button) view.findViewById(R.id.btn_search);
        btn_send = (Button) view.findViewById(R.id.btn_send);
    }
}
