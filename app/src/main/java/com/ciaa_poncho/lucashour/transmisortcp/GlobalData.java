package com.ciaa_poncho.lucashour.transmisortcp;

import java.net.Socket;

public class GlobalData {

	private static GlobalData singleton_instance = null;
	private String ip_address;
	private int port_number;
	private Socket socket;

	protected GlobalData(){
		/* Aplicación de patrón Singleton para mantener una única instancia de la clase
		 * accesible desde cualquier sector de la applicación */
	}

	public static GlobalData getInstance() {
		if(singleton_instance == null) {
			singleton_instance = new GlobalData();
		}
		return singleton_instance;
	}
	
	public void setPortNumber(int num){
		port_number = num;
	}

	public int getPortNumber(){ return port_number;	}

	public String getPortNumberAsString(){ return String.valueOf(port_number); }

	public void setIpAddress(String address){
		ip_address = address;
	}

	public String getIpAddress(){
		return ip_address;
	}

    public void setSocket(Socket tcpSocket){
        socket = tcpSocket;
    }

    public Socket getSocket(){ return socket; }

}
