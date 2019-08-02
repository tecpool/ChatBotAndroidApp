package in.tecpool.chatbot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by umesha on 20/04/2019.
 */

public class BO_SCRIPT_LINK_DETAILS {
    public int id;

    public int scriptId;
    public int srNo;
    public char linkType;
    public String title;
    public String link;

    public int clientId;
    public String createdModifiedDate;
    public char archiveFlag;
    public char readOnly;

    public List<BO_SCRIPT_LINK_DETAILS> jsonToList(String jsonScript) {
        List<BO_SCRIPT_LINK_DETAILS> list = new ArrayList<>();

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonScript);
            JSONObject jsonObj;
            for (int l = 0; l < jsonArray.length(); l++) {
                jsonObj = (JSONObject) jsonArray.get(l);
                BO_SCRIPT_LINK_DETAILS detail = new BO_SCRIPT_LINK_DETAILS();

                detail.id = Integer.parseInt(jsonObj.getString("id"));
                detail.scriptId = Integer.parseInt(jsonObj.getString("scriptId"));
                detail.srNo = Integer.parseInt(jsonObj.getString("srNo"));
                detail.linkType = jsonObj.getString("linkType").toCharArray()[0];
                detail.title = jsonObj.getString("title");
                detail.link = jsonObj.getString("link");
                detail.clientId = Integer.parseInt(jsonObj.getString("clientId"));
                detail.createdModifiedDate = jsonObj.getString("createdModifiedDate");
                detail.archiveFlag = jsonObj.getString("archiveFlag").toCharArray()[0];
                detail.readOnly = jsonObj.getString("readOnly").toCharArray()[0];
                list.add(detail);

            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return list;

    }
}
