package br.com.caelum.contadorhoras.modelo;

import java.io.Serializable;

/**
 * Created by matheus on 10/11/15.
 */
public class Dia implements Serializable {

    private String data;
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data;
    }
}
