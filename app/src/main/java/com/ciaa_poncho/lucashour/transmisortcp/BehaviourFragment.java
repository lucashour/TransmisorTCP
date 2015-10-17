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

import java.io.IOException;
import java.net.Socket;

public class BehaviourFragment extends Fragment implements View.OnClickListener{

    private TextView ip_address;
    private TextView port_number;
    //private Button up;
    //private Button down;
    private Button connect;
    private Button disconnect;
    private SeekBar seekBar;
    private Socket socket;
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
            ip_address.setText(GlobalData.getInstance().getIpAddress());
            port_number = ((TextView) view.findViewById(R.id.port_number_tv));
            port_number.setText(GlobalData.getInstance().getPortNumberAsString());
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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sendDataToSocket(" | " + String.valueOf(progress - 100) + " | ");
            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        connect.setOnClickListener(this);
        disconnect.setOnClickListener(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(false); //Indicamos que este Fragment no tiene su propio menu de opciones
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
                    connectToSocket();
                    break;
                case R.id.disconnect_button:
                    disconnectFromSocket();
                    break;

            }

        }
    }

    private boolean existsIpAddress(){
        if (GlobalData.getInstance().getIpAddress() == null) {
            showToastMessage("Configuración de dirección IP destino requerida.");
            return false;
        }
        return true;
    }

    private boolean isSocketConnected(){
        if (this.socket != null){
            if (this.socket.isConnected())
                return true;
        }
        return false;
    }

    private void connectToSocket(){
        String ipAddress = GlobalData.getInstance().getIpAddress();
        int portNumber = GlobalData.getInstance().getPortNumber();
        if (!isSocketConnected()){
            try {
                socket = new Socket(ipAddress,portNumber);
                showLongToastMessage("Conexión establecida con " + ipAddress + ":" + String.valueOf(portNumber) + ".");
            } catch (IOException e) {
                e.printStackTrace();
                showToastMessage("Error al intentar establecer conexión.");
            }
        }
        else
            showToastMessage("Ya existe una conexión. Es necesario cerrar la conexión existente.");
    }

    private void disconnectFromSocket(){
        if (isSocketConnected()){
            try {
                socket.close();
                showToastMessage("Conexión cerrada exitosamente.");
            } catch (IOException e) {
                e.printStackTrace();
                showToastMessage("Error al intentar finalizar conexión.");
            }
        }
        else
            showToastMessage("No existe conexión.");
    }

    private void sendDataToSocket(String message){
        if (isSocketConnected()){
            TcpAsyncSend tcpCommunication = new TcpAsyncSend(socket,message);
            tcpCommunication.executeOnExecutor(TcpAsyncSend.THREAD_POOL_EXECUTOR);
        }
        else
            showToastMessage("Datos no enviados por no existir conexión.");
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