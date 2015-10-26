package com.ciaa_poncho.lucashour.transmisortcp;

import android.app.Fragment;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class BehaviourFragment extends Fragment implements View.OnClickListener {

    private TextView ip_address;
    private TextView port_number;
    //private Button up;
    //private Button down;
    private Button connect;
    private Button disconnect;
    private SeekBar seekBar;
    private Toast toast;

    public BehaviourFragment(){}

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view =  inflater.inflate(R.layout.fragment_behaviour, container, false);


        if(view != null){

            //up = ((Button) view.findViewById(R.id.up_button));
            //down = ((Button) view.findViewById(R.id.down_button));
            seekBar = ((SeekBar) view.findViewById(R.id.seek_bar));
            ip_address = ((TextView) view.findViewById(R.id.ip_address_tv));
            ip_address.setText(TcpSocketData.getInstance().getIpAddress());
            port_number = ((TextView) view.findViewById(R.id.port_number_tv));
            port_number.setText(TcpSocketData.getInstance().getPortNumberAsString());
            connect = ((Button) view.findViewById(R.id.connect_button));
            disconnect = ((Button) view.findViewById(R.id.disconnect_button));
            toast = new Toast(getActivity().getApplicationContext());
        }

        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //up.setOnClickListener(this);
        //down.setOnClickListener(this);
        seekBar.setMax(200);
        seekBar.setProgress(100);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String result = TcpSocketManager.sendDataToSocket(formatMessageToSend(progress));
                displayInformationMessage(result);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // Sin implementar.
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                // Sin implementar.
            }
        });
        connect.setOnClickListener(this);
        disconnect.setOnClickListener(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(false); //Indicamos que este Fragment no tiene su propio menú de opciones
    }

    public void onClick(View view) {

        if (existsIpAddress()){
            switch (view.getId()){
                /*case R.id.up_button:
                    sendDataToSocket("up");
                    break;
                case R.id.down_button:
                    sendDataToSocket("down");
                    break;*/
                case R.id.connect_button:
                    displayInformationMessage(TcpSocketManager.connectToSocket());
                    break;
                case R.id.disconnect_button:
                    this.seekBar.setProgress(100);
                   displayInformationMessage(TcpSocketManager.disconnectFromSocket());
                    break;

            }

        }
    }

    private boolean existsIpAddress(){
        if (TcpSocketData.getInstance().getIpAddress() == null) {
            showToastMessage("Configuración de dirección IP destino requerida.");
            return false;
        }
        return true;
    }

    private static String formatMessageToSend(int value){
        String string = String.valueOf(value);
        return (String.valueOf(string.length() + 1) + "%" + string);
    }

    private void displayInformationMessage(String message){
        if (!message.equals(""))
            showToastMessage(message);
    }

    private void showToastMessage(String message){
        showToast(message,Toast.LENGTH_SHORT);
    }

    private void showLongToastMessage(String message){
        showToast(message, Toast.LENGTH_LONG);
    }

    private void showToast(String message, int duration){
        if (toast != null)
            toast.cancel();
        toast = Toast.makeText(this.getActivity().getApplicationContext(), message, duration);
        toast.show();
    }

}