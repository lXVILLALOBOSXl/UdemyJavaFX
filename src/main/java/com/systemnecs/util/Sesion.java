package com.systemnecs.util;

import com.systemnecs.model.Usuario;

public class Sesion {
    private static Usuario usuario;
    public static  Usuario getSesion(Usuario usuario){
        if(Sesion.usuario == null){
            Sesion.usuario = usuario;
        }
        return Sesion.usuario;
    }
}
