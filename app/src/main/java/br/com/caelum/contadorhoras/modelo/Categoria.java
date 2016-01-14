package br.com.caelum.contadorhoras.modelo;

/**
 * Created by matheus on 08/12/15.
 */
public class Categoria {

    private String tipo;
    private Long id;

    @Override
    public String toString() {
        return tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
