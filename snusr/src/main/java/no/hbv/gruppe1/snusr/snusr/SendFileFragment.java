package no.hbv.gruppe1.snusr.snusr;

import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Long Huynh on 27.05.2016.
 */
public class SendFileFragment extends Fragment {
    ListView listDevicesView;
    List<BluetoothDevice> listDevices;
    Button btn_send, btn_search;
    BluetoothHandler bHandler = null;
    BluetoothAdapter mBluetoothAdapter = null;
    ArrayAdapter adapter;

    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

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
            }
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get local Bluetooth adapter
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
            // Otherwise, setup the chat session
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bHandler != null) {
            bHandler.stop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        // Performing this check in onResume() covers the case in which BT was
        // not enabled during onStart(), so we were paused to enable it...
        // onResume() will be called when ACTION_REQUEST_ENABLE activity returns.
        if (bHandler != null) {
            // Only if the state is STATE_NONE, do we know that we haven't started already
            if (bHandler.getState() == BluetoothHandler.STATE_NONE) {
                // Start the Bluetooth services
                bHandler.start();
            }
        }
    }

    private void connectDevice(String address) {
        mBluetoothAdapter.cancelDiscovery();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        // Attempt to connect to the device
        bHandler.connect(device, MY_UUID);
    }

     @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         final View rootview = inflater.inflate(R.layout.fragment_send, container, false);
         listDevicesView = (ListView) rootview.findViewById(R.id.list_devices);
         btn_search = (Button) rootview.findViewById(R.id.btn_search);
         btn_send = (Button) rootview.findViewById(R.id.btn_send);
         listDevices = new ArrayList();
         adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, listDevices);

         listDevicesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
             @Override
             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                 connectDevice(listDevices.get(position).getAddress());
             }
         });

         btn_send.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
                 // If there are paired devices
                 if (pairedDevices.size() > 0) {
                     // Loop through paired devices
                     for (BluetoothDevice device : pairedDevices) {
                         // Add the name and address to an array adapter to show in a ListView
                         adapter.add(device.getName() + "\n" + device.getAddress());
                         listDevices.add(device);
                     }
                 }
                 listDevicesView.setAdapter(adapter);
             }
         });
         btn_search.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                // if (isChecked) {
                     adapter.clear();
                    listDevices.clear();
                     getActivity().registerReceiver(mReceiver, filter);
                     mBluetoothAdapter.startDiscovery();
                   //  isChecked = false;
                 //} else {
                   //  getActivity().unregisterReceiver(mReceiver);
                   //  mBluetoothAdapter.cancelDiscovery();
                  //   isChecked = true;
               //  }
                 listDevicesView.setAdapter(adapter);
             }
         });
         return rootview;
     }

}
