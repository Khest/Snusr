package no.hbv.gruppe1.snusr.snusr;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.*;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import no.hbv.gruppe1.snusr.snusr.dataclasses.*;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Long Huynh on 27.05.2016.
 */
@SuppressWarnings("Duplicates")
public class SendFileFragment extends Fragment {
    private ListView listDevicesView;
    private List<BluetoothDevice> listDevices;
    private Button btn_send, btn_search;
    private BluetoothHandler2 bluetoothHandler = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private ArrayAdapter<String> adapter;
    private DatabaseInteractor interactor;

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

    private String savedState = "";

    @SuppressLint("HandlerLeak")
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
                                "Connected to " + msg.getData().getString("0"),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Globals.MESSAGE_READ:
                    byte[] buffer = (byte[]) msg.obj;
                    String incoming = new String(buffer, 0, msg.arg1);
                    //String savedState = "";
                    try {
                        if (!incoming.contains(END_OF_ENTRY_DELIMITER)) {
                            savedState += incoming;
                        } else {
                            synchronized (savedState) {
                                doStuff(savedState);
                                savedState = "";
                            }
                        }


                    } catch (Exception ex) {
                        Log.e(Globals.TAG, "Fatal error when receiving data, exiting " + ex.getMessage());
                        throw new RuntimeException(ex);
                    } finally {

                    }
                    //Toast.makeText(fragmentActivity, "Incoming: " + incoming, Toast.LENGTH_SHORT).show();
                    break;
                case Globals.MESSAGE_WRITE:
                    byte[] buffer2 = (byte[]) msg.obj;

                    String outgoing = new String(buffer2);

                    //Toast.makeText(fragmentActivity, "Outgoing: " + outgoing, Toast.LENGTH_SHORT).show();
                    break;
                case Globals.MESSAGE_TOAST:
                    if (fragmentActivity != null) {
                        Toast.makeText(fragmentActivity, msg.getData().getString(Globals.TOAST), Toast.LENGTH_SHORT).show();
                    }
            }

        }
    };

    void doStuff(String incoming) {
        incoming = incoming.substring(0, incoming.length()-END_OF_ENTRY_DELIMITER.length());
        String arr1[] = incoming.split("\\"+ DELIMITER);
        //Log.i(Globals.TAG, " " + incoming);
//        Log.i(Globals.TAG, " " + arr1[0] + " : " + arr1.length);
//        Log.i(Globals.TAG, " " + Arrays.toString(arr1));

        List<Filtration> filtrations = new ArrayList<>();
        Filtration f = Filtration.MANUFACTURER;
        f.setSearchValue(arr1[12]);
        filtrations.add(f);
        Cursor checkManufacturer = interactor.fetchSnus(filtrations, null);
        long manufacturerNumber = Long.valueOf(arr1[2]);
        if (checkManufacturer != null) {
            if (checkManufacturer.getCount() == 0) {
                Log.i(Globals.TAG, "Manufacturer " + arr1[12] + " does not exist, creating");
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.FeedEntry.col_manufacturer_name, arr1[12]);
                cv.put(DatabaseHelper.FeedEntry.col_manufacturer_country, arr1[13]);
                cv.put(DatabaseHelper.FeedEntry.col_manufacturer_url, arr1[14]);
                manufacturerNumber = interactor.insert(DatabaseHelper.FeedEntry.DATABASE_TABLE_MANUFACTURER, cv);
            } else {
                while (checkManufacturer.moveToNext()) {
                    if (checkManufacturer.getString(checkManufacturer.getColumnIndex(DatabaseHelper.FeedEntry.col_manufacturer_name))
                            .equals(arr1[12])) {
                        manufacturerNumber = checkManufacturer.getLong(checkManufacturer.getColumnIndex(DatabaseHelper.FeedEntry.col_manufacturer_id));
                        Log.i(Globals.TAG, "Manufacturer exists, setting position to " + String.valueOf(manufacturerNumber));
                        break;
                    }
                }
            }
        }
        if (checkManufacturer != null) {
            //checkManufacturer.close();
        }
        f = Filtration.LINE_TEXT;
        f.setSearchValue(arr1[16]);
        filtrations.set(0, f);
        Cursor checkLine = interactor.fetchSnus(filtrations, null);
        long lineNumber = Long.valueOf(arr1[15]);
        if (checkLine != null) {
            if (checkLine.getCount() == 0) {
                Log.i(Globals.TAG, "Line " + arr1[15] + " does not exist, creating");
                ContentValues cv = new ContentValues();
                cv.put(DatabaseHelper.FeedEntry.col_line_name, arr1[16]);
                cv.put(DatabaseHelper.FeedEntry.col_manufacturer_id, manufacturerNumber);
                lineNumber = interactor.insert(DatabaseHelper.FeedEntry.DATABASE_TABLE_LINE, cv);
            } else {
                while (checkLine.moveToNext()) {
                    if (checkLine.getString(checkLine.getColumnIndex(DatabaseHelper.FeedEntry.col_line_name))
                        .equals(arr1[16])) {
                        lineNumber = checkLine.getLong(checkLine.getColumnIndex(DatabaseHelper.FeedEntry.col_line_id));
                        Log.i(Globals.TAG, "Line exists, setting position to " + String.valueOf(lineNumber));
                        break;
                    }
                }
            }
        }
        if (checkLine != null) {
            //checkLine.close();
        }
        Log.i(Globals.TAG, "Searching for the snus based on current information");
        Filtration f1 = Filtration.MANUFACTURER_NUMBER;
        f1.setSearchValue(manufacturerNumber);
        Filtration f2 = Filtration.LINE_NUMBER;
        f2.setSearchValue(lineNumber);
        Filtration f3 = Filtration.NAME;
        f3.setSearchValue(arr1[1]);
        Filtration f4 = Filtration.TYPE_NUMBER;
        f4.setSearchValue(arr1[10]);
        Filtration f5 = Filtration.TASTE_NUMBER;
        f5.setSearchValue(arr1[4]);
        filtrations.clear();
        filtrations.add(f1);
        filtrations.add(f2);
        filtrations.add(f3);
        filtrations.add(f4);
        filtrations.add(f5);
        Cursor checkSnus = interactor.fetchSnus(filtrations, null);
        Log.i(Globals.TAG, DatabaseUtils.dumpCursorToString(checkSnus));
        Log.i(Globals.TAG, "Found number of snus " + checkSnus.getCount());
        if (checkSnus.getCount() >= 1) {
            Log.i(Globals.TAG, "Snus exists, averaging totalrank");
            ContentValues averageTotalRank = new ContentValues();
            checkSnus.moveToFirst();
            double currentAverageRank = checkSnus.getDouble(checkSnus.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_totalrank));
            double incomingAverageRank = Double.valueOf(arr1[9]);
            double newAverageRank = (currentAverageRank + incomingAverageRank) / 2;
            averageTotalRank.put(DatabaseHelper.FeedEntry.col_snus_totalrank, newAverageRank);
            interactor.update(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS,
                    averageTotalRank,
                    DatabaseHelper.FeedEntry.col_snus_id + "=" + arr1[0], null);
        } else {
            Log.i(Globals.TAG, "This snus is new, we'll insert it into the database");
            ContentValues cv = new ContentValues();
            cv.put(DatabaseHelper.FeedEntry.col_snus_name, arr1[1]);
            cv.put(DatabaseHelper.FeedEntry.col_snus_manufactorer, Integer.valueOf(arr1[2]));
            cv.put(DatabaseHelper.FeedEntry.col_snus_line, arr1[3]);
            cv.put(DatabaseHelper.FeedEntry.col_snus_taste1, arr1[4]);
            cv.put(DatabaseHelper.FeedEntry.col_snus_taste2, arr1[5]);
            cv.put(DatabaseHelper.FeedEntry.col_snus_taste3, arr1[6]);
            cv.put(DatabaseHelper.FeedEntry.col_snus_strength, arr1[7]);
            cv.put(DatabaseHelper.FeedEntry.col_snus_nicotinelevel, arr1[8]);
            cv.put(DatabaseHelper.FeedEntry.col_snus_totalrank, arr1[9]);
            cv.put(DatabaseHelper.FeedEntry.col_snus_type, arr1[10]);
            if (arr1.length == 19) {
                cv.put(DatabaseHelper.FeedEntry.col_snus_img, arr1[18]);
                Log.i(Globals.TAG, "We found an image to insert");
            }

            long newSnusId = interactor.insert(DatabaseHelper.FeedEntry.DATABASE_TABLE_SNUS, cv);
            Log.i(Globals.TAG, "Inserted new snus with id " + String.valueOf(newSnusId));
        }
        if (checkSnus != null) {
            //checkSnus.close();
        }
    }
// 0   DatabaseHelper.FeedEntry.col_snus_id,
// 1   DatabaseHelper.FeedEntry.col_snus_name,
// 2   DatabaseHelper.FeedEntry.col_snus_manufactorer,
// 3   DatabaseHelper.FeedEntry.col_snus_line,
// 4   DatabaseHelper.FeedEntry.col_snus_taste1,
// 5   DatabaseHelper.FeedEntry.col_snus_taste2,
// 6   DatabaseHelper.FeedEntry.col_snus_taste3,
// 7   DatabaseHelper.FeedEntry.col_snus_strength,
// 8   DatabaseHelper.FeedEntry.col_snus_nicotinelevel,
// 9   DatabaseHelper.FeedEntry.col_snus_totalrank,
// 10  DatabaseHelper.FeedEntry.col_snus_type,
// 11   DatabaseHelper.FeedEntry.col_manufacturer_id,
// 12   DatabaseHelper.FeedEntry.col_manufacturer_name,
// 13   DatabaseHelper.FeedEntry.col_manufacturer_country,
// 14   DatabaseHelper.FeedEntry.col_manufacturer_url,
// 15   DatabaseHelper.FeedEntry.col_line_id,
// 16   DatabaseHelper.FeedEntry.col_line_name,
// 17   DatabaseHelper.FeedEntry.col_line_manufactorer

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            Toast.makeText(getActivity(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
        }
        this.interactor = new DatabaseInteractor(getActivity());
    }

    @Override
    public void onDestroyView() {
        interactor.close();
        super.onDestroyView();
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
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (bluetoothHandler != null) {
            bluetoothHandler.stop();
        }
        interactor.close();
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
        bluetoothHandler.connect(mBluetoothAdapter.getRemoteDevice(address));
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
                connectDevice(listDevices.get(position).getAddress());
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothHandler.getStatus() == BluetoothHandler2.STATE_CONNECTED) {
                    try {
                        Sorting sorting = Sorting.OLDESTTONEWEST;
                        sorting.setOrder(Sorting.Order.ASCENDING);
                        Cursor snusCursor = interactor.fetchSnus(null, sorting);

                        //while (snusCursor.moveToNext()) {
                        snusCursor.moveToFirst();
                            StringBuilder sb = new StringBuilder();
                            for (String s : snusListOrder) {
                                sb.append(snusCursor.getString(snusCursor.getColumnIndex(s))).append(DELIMITER);
                            }
                            byte[] b = snusCursor.getBlob(snusCursor.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_img));
                            //String s = new String(b, "ISO-8859-1");
                            //Log.i(Globals.TAG, " Containing: " + sb.toString());
                            if (b != null) {
                                sb.append(new String(b, "ISO-8859-1"));
                            } else {
                                Log.i(Globals.TAG, "No image attached for " + snusCursor.getString(0));
                            }
                            //sb.append(c.getBlob(c.getColumnIndex(DatabaseHelper.FeedEntry.col_snus_img))).append(DELIMITER);
                            sb.append(END_OF_ENTRY_DELIMITER);
                            Log.i(Globals.TAG, " Sending data for ID : " + snusCursor.getString(0));
                            bluetoothHandler.write(sb.toString().getBytes());

                        //}
                        //snusCursor.close(); // FIXME: 2016-05-30 closing this here ruins file transfer
                    } catch (UnsupportedEncodingException uex) {
                        Log.e(Globals.TAG, "Fatal error when converting byte to string " + uex.getMessage());
                    } catch (Exception ex) {
                        Log.e(Globals.TAG, "Fatal error " + ex.getMessage());
                    }

                } else {
                    Toast.makeText(getActivity(), " no't connected to anything", Toast.LENGTH_SHORT).show();
                }
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

    private static final String DELIMITER = "|";
    private static final String END_OF_ENTRY_DELIMITER = "|||||";



    private static final String snusListOrder[] = {
            DatabaseHelper.FeedEntry.col_snus_id,
            DatabaseHelper.FeedEntry.col_snus_name,
            DatabaseHelper.FeedEntry.col_snus_manufactorer,
            DatabaseHelper.FeedEntry.col_snus_line,
            DatabaseHelper.FeedEntry.col_snus_taste1,
            DatabaseHelper.FeedEntry.col_snus_taste2,
            DatabaseHelper.FeedEntry.col_snus_taste3,
            DatabaseHelper.FeedEntry.col_snus_strength,
            DatabaseHelper.FeedEntry.col_snus_nicotinelevel,
            DatabaseHelper.FeedEntry.col_snus_totalrank,
            DatabaseHelper.FeedEntry.col_snus_type,
            DatabaseHelper.FeedEntry.col_manufacturer_id,
            DatabaseHelper.FeedEntry.col_manufacturer_name,
            DatabaseHelper.FeedEntry.col_manufacturer_country,
            DatabaseHelper.FeedEntry.col_manufacturer_url,
            DatabaseHelper.FeedEntry.col_line_id,
            DatabaseHelper.FeedEntry.col_line_name,
            DatabaseHelper.FeedEntry.col_line_manufactorer
    };

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
