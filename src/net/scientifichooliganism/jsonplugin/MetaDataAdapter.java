package net.scientifichooliganism.jsonplugin;

import com.google.gson.*;
import net.scientifichooliganism.javaplug.interfaces.MetaData;
import net.scientifichooliganism.javaplug.vo.BaseMetaData;

import java.lang.reflect.Type;

public class MetaDataAdapter implements JsonDeserializer<MetaData>, JsonSerializer<MetaData> {
    @Override
    public MetaData deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        MetaData metaData = new BaseMetaData();
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement element;

        if((element = jsonObject.get("object")) != null){
            metaData.setObject(element.getAsString());
        }
        if((element = jsonObject.get("object_id")) != null){
            metaData.setObjectID(element.getAsString());
        }
        if((element = jsonObject.get("sequence")) != null){
            metaData.setSequence(element.getAsInt());
        }
        if((element = jsonObject.get("key")) != null){
            metaData.setKey(element.getAsString());
        }
        if((element = jsonObject.get("value")) != null){
            metaData.setValue(element.getAsString());
        }
        if((element = jsonObject.get("id")) != null){
            metaData.setID(element.getAsString());
        }
        if((element = jsonObject.get("label")) != null){
            metaData.setLabel(element.getAsString());
        }

        return metaData;
    }

    @Override
    public JsonElement serialize(MetaData metaData, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject object = new JsonObject();
        if(metaData.getObject() != null){
            object.addProperty("object", metaData.getObject());
        }
        if(metaData.getObjectID() != null){
            object.addProperty("object_id", metaData.getObjectID());
        }
        if(metaData.getSequence() != -1){
            object.addProperty("sequence", metaData.getSequence());
        }
        if(metaData.getKey() != null){
            object.addProperty("key", metaData.getKey());
        }
        if(metaData.getValue() != null){
            object.addProperty("value", metaData.getValue());
        }
        if(metaData.getID() != null){
            object.addProperty("id", metaData.getID());
        }
        if(metaData.getLabel() != null){
            object.addProperty("label", metaData.getLabel());
        }

        return object;
    }
}
