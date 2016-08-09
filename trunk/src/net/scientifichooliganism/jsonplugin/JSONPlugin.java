package net.scientifichooliganism.jsonplugin;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.scientifichooliganism.javaplug.annotations.Param;
import net.scientifichooliganism.javaplug.interfaces.*;
import net.scientifichooliganism.javaplug.vo.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Vector;

public class JSONPlugin implements Plugin {
	Gson gson;

	public JSONPlugin () {
		gson = new GsonBuilder().setPrettyPrinting()
				.setFieldNamingStrategy(new JavaPlugFieldNamingStrategy())
				.registerTypeAdapter(MetaData.class, new MetaDataAdapter())
				.create();
	}

	@Override
	public String[][] getActions() {
		return new String[0][];
	}

	public String jsonFromObject(@Param(name="object") Object object){
		String objectName = null;
		if(ValueObject.class.isAssignableFrom(object.getClass())){
			objectName = stringFromObject((ValueObject)object);
		} else if(Collection.class.isAssignableFrom(object.getClass())) {
			Vector<String> jsonStrings = new Vector<>();
			for(Object o : (Collection)object){
				jsonStrings.add(jsonFromObject(o));
			}
			String json = "{\"data\":[";
			for(String s : jsonStrings){
				json += s;
				json += ",";
			}
			json = json.substring(0, json.length() - 1);
			json += "]}";
			return json;
		} else {
			objectName = object.getClass().getSimpleName();
		}

		return "{\"" + objectName + "\":" + gson.toJson(object) + "}";
	}

	private Object objectFromJson(String json, Type type) {
		return gson.fromJson(json, type);
	}

	public Object objectFromJson(@Param(name="json") String json) {
		JsonParser parser = new JsonParser();
		JsonObject object = parser.parse(json).getAsJsonObject();
		Collection ret = new ArrayList();
		for(Map.Entry<String, JsonElement> entry : object.entrySet()){
			try {
				Type type = classFromString(entry.getKey());
				Object instance = gson.fromJson(entry.getValue(), type);
				ret.add(instance);
			} catch (Exception exc){
				exc.printStackTrace();
			}
		}

		if(ret.size() == 1){
			return ret.iterator().next();
		} else {
			return ret;
		}
	}

	private <T extends ValueObject> String stringFromObject(T object){
		if(object instanceof Action) {
		    return "action";
		} else if(Application.class.isAssignableFrom(object.getClass())){
		    return "application";
		} else if(Block.class.isAssignableFrom(object.getClass())){
			return "block";
		} else if(Configuration.class.isAssignableFrom(object.getClass())){
			return "configuration";
		} else if(Environment.class.isAssignableFrom(object.getClass())){
			return "environment";
		} else if(Event.class.isAssignableFrom(object.getClass())){
			return "event";
		} else if(MetaData.class.isAssignableFrom(object.getClass())){
			return "meta-data";
		} else if(Release.class.isAssignableFrom(object.getClass())){
		    return "release";
		} else if(Task.class.isAssignableFrom(object.getClass())){
			return "task";
		} else if(TaskCategory.class.isAssignableFrom(object.getClass())){
			return "task_category";
		} else if(ValueObject.class.isAssignableFrom(object.getClass())){
			return "value_object";
		}

		return null;
	}

	private Type classFromString(String className) throws ClassNotFoundException{
		switch(className.toLowerCase()){
			case "action":
				return BaseAction.class;
			case "actions":
				return new TypeToken<Collection<BaseAction>>(){}.getType();
			case "application":
				return BaseApplication.class;
			case "applications":
				return new TypeToken<Collection<BaseApplication>>(){}.getType();
			case "block":
				return BaseBlock.class;
			case "blocks":
				return new TypeToken<Collection<BaseBlock>>(){}.getType();
			case "configuration":
				return BaseConfiguration.class;
			case "configurations":
				return new TypeToken<Collection<BaseConfiguration>>(){}.getType();
			case "environment":
				return BaseEnvironment.class;
			case "environments":
				return new TypeToken<Collection<BaseEnvironment>>(){}.getType();
			case "event":
				return BaseEvent.class;
			case "events":
				return new TypeToken<Collection<BaseEvent>>(){}.getType();
			case "meta-data":
				return BaseMetaData.class;
			case "release":
				return BaseRelease.class;
			case "releases":
				return new TypeToken<Collection<BaseRelease>>(){}.getType();
			case "task":
				return BaseTask.class;
			case "tasks":
				return new TypeToken<Collection<BaseTask>>(){}.getType();
			case "task_category":
				return BaseTaskCategory.class;
			case "task_categories":
				return new TypeToken<Collection<BaseTaskCategory>>(){}.getType();
			case "transaction":
				return BaseTransaction.class;
			case "transactions":
				return new TypeToken<Collection<BaseTransaction>>(){}.getType();
			case "value_object":
				return BaseValueObject.class;
			case "value_objects":
				return new TypeToken<Collection<BaseValueObject>>(){}.getType();
			default:
				return Class.forName(className);
		}
	}

	public static void main(String [] args){
		JSONPlugin plugin = new JSONPlugin();
		MetaData data1 = new BaseMetaData();
		MetaData data2 = new BaseMetaData();
        data1.setKey("key1");
		data2.setKey("key2");
        data1.setValue("value1");
		data2.setValue("value2");
        data1.setSequence(0);
		data2.setSequence(1);

		data1.setID("12");
		data1.setObject("object");
		data1.setObjectID("542");
		data1.setLabel("label");
		Action action = new BaseAction();
		action.setID(String.valueOf(42));
		action.setLabel("Default Label");
		action.setDescription("This is my description");
		action.setModule("Awesome module!");
		action.setKlass("Class of 2016");
		action.setURL("www.google.com");
		action.setMethod("myMethod");
		action.addMetaData(data1);
		action.addMetaData(data2);
		String json = plugin.jsonFromObject(action);
		System.out.println(json);
		Action object = (Action)plugin.objectFromJson(json);


		System.out.println(json);
		System.out.println(object);
//		System.out.println(classObject);
	}
}
