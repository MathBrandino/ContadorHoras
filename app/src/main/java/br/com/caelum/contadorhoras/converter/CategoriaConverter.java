package br.com.caelum.contadorhoras.converter;

import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.caelum.contadorhoras.modelo.Categoria;

/**
 * Created by matheus on 14/12/15.
 */
public class CategoriaConverter {


    private static final String CATEGORIA = "Categoria";
    private static final String ID = "id";
    private static final String TIPO = "tipo";

    public List<Categoria> fromJson(String json) {

        List<Categoria> categorias = new ArrayList<>();

        try {
            JSONObject jsonComTodasAsCategorias = new JSONObject(json);
            JSONArray jsonArray = jsonComTodasAsCategorias.getJSONArray(CATEGORIA);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonCategoria = jsonArray.getJSONObject(i);

                categorias.add(criaCategoriaPeloJsonObject(jsonCategoria));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return categorias;
    }

    @NonNull
    private Categoria criaCategoriaPeloJsonObject(JSONObject jsonCategoria) throws JSONException {
        Categoria categoria = new Categoria();
        categoria.setId(jsonCategoria.getLong(ID));
        categoria.setTipo(jsonCategoria.getString(TIPO));
        return categoria;
    }
}
