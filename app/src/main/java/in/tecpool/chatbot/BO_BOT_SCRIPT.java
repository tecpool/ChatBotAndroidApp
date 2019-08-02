package in.tecpool.chatbot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by umesha on 20/04/2019.
 */

public class BO_BOT_SCRIPT {
    public int id;

    public int botId;
    public int srNo;
    public int typeId;
    public String message;
    public double minValue;
    public double maxValue;
    public double step;
    public int redirectId;
    public int failedId;
    public String textValidation;
    public char isAnswered;
    public int clientId;
    public String createdModifiedDate;
    public char archiveFlag;
    public char readOnly;
    public List<BO_SCRIPT_DETAILS> scriptDetails;
    public List<BO_SCRIPT_LINK_DETAILS> scriptLinkDetails;


    public BO_BOT_SCRIPT addAnswer(String answer) {
        try {
            BO_BOT_SCRIPT s=new BO_BOT_SCRIPT();
            s.id =0;
            s.botId = 0;
            s.srNo =0;
            s.typeId =1000;
            s.message =answer;
            s.minValue =0;
            s.maxValue = 0;
            s.step =0;
            s.redirectId = 0;
            s.failedId =0;
            s.textValidation ="";
            s.clientId =0;
            s.createdModifiedDate ="";
            s.archiveFlag ='F';
            s.readOnly = 'Y';
            s.scriptDetails = null;
            s.scriptLinkDetails = null;
            return s;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public BO_BOT_SCRIPT() {

    }
    public BO_BOT_SCRIPT(String script) {

            if (script.equals("wait")) {
                this.id = 0;
                this.typeId =0;

            } else {
                try {

                    JSONObject object = new JSONObject(script);

                this.id = Integer.parseInt(object.getString("id"));
                this.botId = Integer.parseInt(object.getString("botId"));
                this.srNo = Integer.parseInt(object.getString("srNo"));
                this.typeId = Integer.parseInt(object.getString("typeId"));
                this.message = object.getString("message");
                this.minValue = Double.parseDouble(object.getString("minValue"));
                this.maxValue = Double.parseDouble(object.getString("maxValue"));
                this.step = Double.parseDouble(object.getString("step"));
                this.redirectId = Integer.parseInt(object.getString("redirectId"));
                this.failedId = Integer.parseInt(object.getString("failedId"));
                this.textValidation = object.getString("textValidation");
                this.clientId = Integer.parseInt(object.getString("clientId"));
                this.createdModifiedDate = object.getString("createdModifiedDate");
                this.archiveFlag = object.getString("archiveFlag").toCharArray()[0];
                this.readOnly = object.getString("readOnly").toCharArray()[0];
                this.isAnswered = 'N';
                BO_SCRIPT_DETAILS scriptDetails1 = new BO_SCRIPT_DETAILS();

                this.scriptDetails = scriptDetails1.jsonToList(object.getString("scriptDetails"));
                BO_SCRIPT_LINK_DETAILS scriptLinkDetails1 = new BO_SCRIPT_LINK_DETAILS();

                this.scriptLinkDetails = scriptLinkDetails1.jsonToList(object.getString("scriptLinkDetails"));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }


}
