package utfpr.converter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import utfpr.model.Municipio;

public class municipioconverte {
    public List<Municipio> converter(String json) {
        JSONArray array = new JSONArray(json);
        List<Municipio> lista = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);

            JSONObject uf = localizarUF(o);
            String sigla = "-";
            String regiao = "-";

            if (uf != null) {
                sigla = uf.optString("sigla", "-");
                JSONObject objRegiao = uf.optJSONObject("regiao");
                if (objRegiao != null) {
                    regiao = objRegiao.optString("nome", "-");
                }
            }

            lista.add(new Municipio(
                    o.getInt("id"),
                    o.getString("nome"),
                    sigla,
                    regiao));
        }
        return lista;
    }


    private JSONObject localizarUF(JSONObject Municipio) {

        JSONObject micro = Municipio.optJSONObject("microrregiao");
        if (micro != null) {
            JSONObject meso = micro.optJSONObject("mesorregiao");
            if (meso != null) {
                JSONObject uf = meso.optJSONObject("UF");
                if (uf != null) {
                    return uf;
                }
            }
        }


        JSONObject imediata = Municipio.optJSONObject("regiao-imediata");
        if (imediata != null) {
            JSONObject intermediaria = imediata.optJSONObject("regiao-intermediaria");
            if (intermediaria != null) {
                return intermediaria.optJSONObject("UF");
            }
        }

        return null;
    }
}
