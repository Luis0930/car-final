import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.udem.car_final.CarInterface;
import com.udem.car_final.R;

import java.io.IOException;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements CarInterface {

    private Button btnAvanzar, btnIzquierda, btnDerecha, btnRetroceder;
    private EditText editTextIP;
    private TextView txtConectado;
    private ToggleButton toggleConectarse;

    private WifiManager wifiManager;
    private WifiInfo wifiInfo;

    private boolean isConnected = false;
    private String ipAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Solicitar permisos necesarios
        requestPermissions();

        // Obtener referencias a los elementos de la interfaz
        btnAvanzar = findViewById(R.id.avanzar);
        btnIzquierda = findViewById(R.id.izquierda);
        btnDerecha = findViewById(R.id.derecha);
        btnRetroceder = findViewById(R.id.retroceder);
        editTextIP = findViewById(R.id.ip);
        txtConectado = findViewById(R.id.conectado);
        toggleConectarse = findViewById(R.id.conectarse);

        // Configurar los listeners de clic para los botones
        btnAvanzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    // Lógica para avanzar el carro
                    avanzar();
                } else {
                    Toast.makeText(MainActivity.this, "No estás conectado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnIzquierda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    // Lógica para girar a la izquierda el carro
                    izquierda();
                } else {
                    Toast.makeText(MainActivity.this, "No estás conectado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnDerecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    // Lógica para girar a la derecha el carro
                    derecha();
                } else {
                    Toast.makeText(MainActivity.this, "No estás conectado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRetroceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isConnected) {
                    // Lógica para retroceder el carro
                    retroceder();
                } else {
                    Toast.makeText(MainActivity.this, "No estás conectado", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Obtener el estado de la conexión WiFi
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();

        // Actualizar el estado de la conexión WiFi
        updateWiFiStatus();

        // Configurar el listener de clic para el botón Conectar/Desconectar
        toggleConectarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (toggleConectarse.isChecked()) {
                    // El toggle está activado (Conectar)
                    conectar(editTextIP.getText().toString());
                } else {
                    // El toggle está desactivado (Desconectar)
                    desconectar();
                }
            }
        });
    }

    // Método para solicitar los permisos necesarios
    private void requestPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_WIFI_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_WIFI_STATE}, 1);
        }

        permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CHANGE_WIFI_STATE}, 2);
        }
    }

    // Método para actualizar el estado de la conexión WiFi
    private void updateWiFiStatus() {
        if (wifiManager.isWifiEnabled()) {
            String ssid = wifiInfo.getSSID().replace("\"", "");
            txtConectado.setText("Estado: Conectado a " + ssid);
            isConnected = true;
        } else {
            txtConectado.setText("Estado: Desconectado");
            isConnected = false;
        }
    }

    // Implementación de los métodos de la interfaz CarInterface

    @Override
    public void conectar(String direccionIp) {
        // Verificar si ya está conectado
        if (isConnected) {
            Toast.makeText(MainActivity.this, "Ya estás conectado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validar la dirección IP ingresada
        if (direccionIp.isEmpty()) {
            Toast.makeText(MainActivity.this, "Ingresa una dirección IP válida", Toast.LENGTH_SHORT).show();
            return;
        }

        @Override
        public void conectar(String direccionIp) {
            // Verificar si ya está conectado
            if (isConnected) {
                Toast.makeText(MainActivity.this, "Ya estás conectado", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar la dirección IP ingresada
            if (direccionIp.isEmpty()) {
                Toast.makeText(MainActivity.this, "Ingresa una dirección IP válida", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lógica para conectarse al carro usando la dirección IP ingresada
            try {
                // Crear un objeto Socket con la dirección IP y un puerto específico del carro
                Socket socket = new Socket(direccionIp, PUERTO_CARRO);

                // Realizar cualquier configuración adicional del socket, como timeouts, etc.

                // Establecer la conexión con el carro
                socket.connect();

                // Aquí puedes agregar más lógica relacionada con la comunicación con el carro

                // enviar un comando al carro para iniciar la comunicación
                enviarComandoAlCarro(socket, "INICIAR");

                // Mostrar un mensaje de éxito
                Toast.makeText(MainActivity.this, "Conexión exitosa", Toast.LENGTH_SHORT).show();

                // Realizar cualquier otra acción necesaria después de establecer la conexión

                // Actualizar el estado de conexión
                toggleConectarse.setEnabled(false);
                editTextIP.setEnabled(false);
                isConnected = true;
            } catch (IOException e) {
                // Error al establecer la conexión con el carro
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Error al conectar con el carro", Toast.LENGTH_SHORT).show();
            }
        }

        class CarConnection {

            private static final int CAR_PORT = 1234; // Puerto del carro
            private static final String CAR_IP = "192.168.0.100"; // Dirección IP del carro

            private Socket socket;
            private InputStream inputStream;
            private OutputStream outputStream;

            public void connectToCar() {
                try {
                    // Crear un objeto Socket con la dirección IP y el puerto del carro
                    socket = new Socket(CAR_IP, CAR_PORT);

                    // Obtener los flujos de entrada y salida del socket
                    inputStream = socket.getInputStream();
                    outputStream = socket.getOutputStream();



                    // Establecer la conexión con el carro
                    // En este ejemplo, enviamos un comando para iniciar la comunicación
                    sendCommandToCar("CONNECT");

                    // Mostrar un mensaje de éxito o realizar cualquier acción necesaria
                    System.out.println("Conexión exitosa");

                } catch (IOException e) {
                    // Error al establecer la conexión con el carro
                    e.printStackTrace();
                }
            }

            public void sendCommandToCar(String command) {
                try {
                    // Convertir el comando en bytes y enviarlo al carro
                    byte[] commandBytes = command.getBytes();
                    outputStream.write(commandBytes);
                    outputStream.flush();

                    // Realizar cualquier lógica adicional después de enviar el comando

                } catch (IOException e) {
                    // Error al enviar el comando al carro
                    e.printStackTrace();
                }
            }

            public void disconnectFromCar() {
                try {
                    // Cerrar los flujos de entrada y salida
                    if (inputStream != null)
                        inputStream.close();
                    if (outputStream != null)
                        outputStream.close();

                    // Cerrar el socket
                    if (socket != null)
                        socket.close();

                    // Realizar cualquier otra acción necesaria después de desconectar

                } catch (IOException e) {
                    // Error al cerrar la conexión
                    e.printStackTrace();
                }
            }
        }
        // Puedes utilizar la variable "direccionIp" para obtener la dirección IP ingresada


        // conectarAlCarro(direccionIp);

        Toast.makeText(MainActivity.this, "Conectando a " + direccionIp, Toast.LENGTH_SHORT).show();
        toggleConectarse.setEnabled(false);
        editTextIP.setEnabled(false);
        isConnected = true;
    }

    private void enviarComandoAlCarro(Socket socket, String iniciar) {

    }

    @Override
    public void desconectar() {
        // Verificar si ya está desconectado
        if (!isConnected) {
            Toast.makeText(MainActivity.this, "Ya estás desconectado", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lógica para desconectarse del carro
        // Aquí puedes agregar tu código para cerrar la conexión con el carro

        // Ejemplo de código para cerrar la conexión:
        // desconectarDelCarro();

        Toast.makeText(MainActivity.this, "Desconectando", Toast.LENGTH_SHORT).show();
        toggleConectarse.setEnabled(true);
        editTextIP.setEnabled(true);
        isConnected = false;
    }

    @Override
    public void avanzar() {
        // Lógica para avanzar el carro
        Toast.makeText(MainActivity.this, "Avanzar", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void retroceder() {
        // Lógica para retroceder el carro
        Toast.makeText(MainActivity.this, "Retroceder", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void izquierda() {
        // Lógica para girar a la izquierda el carro
        Toast.makeText(MainActivity.this, "Izquierda", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void derecha() {
        // Lógica para girar a la derecha el carro
        Toast.makeText(MainActivity.this, "Derecha", Toast.LENGTH_SHORT).show();
    }
}
