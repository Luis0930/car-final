package com.udem.car_final;

public interface CarInterface {
    void conectar(String direccionIp);
    void desconectar();
    void avanzar();
    void retroceder();
    void izquierda();
    void derecha();
}
