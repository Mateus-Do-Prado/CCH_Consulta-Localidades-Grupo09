package utfpr.converter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import utfpr.model.UF;

public class UFconverte {
    public List<UF> converter(String json) {
        JSONArray array = new JSONArray(json);
        List<UF> lista = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            lista.add(new UF(
                    o.getInt("id"),
                    o.getString("nome"),
                    o.getString("sigla")));
        }
        return lista;
    }
}
