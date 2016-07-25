package net.scientifichooliganism.jsonplugin;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.scientifichooliganism.javaplug.interfaces.Action;
import net.scientifichooliganism.javaplug.interfaces.Plugin;
import net.scientifichooliganism.javaplug.vo.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JSONPlugin implements Plugin {
	Gson gson;

	public JSONPlugin () {
		gson = new GsonBuilder().setPrettyPrinting()
				.setFieldNamingStrategy(new JavaPlugFieldNamingStrategy())
				.create();
	}

	@Override
	public String[][] getActions() {
		return new String[0][];
	}

	public String jsonFromObject(Object object){
		return gson.toJson(object);
	}

	public Object objectFromJson(String json, Type type) {
		return gson.fromJson(json, type);
	}

	public Object objectFromJson(String json) {
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
		Action action = new BaseAction();
		action.setID(42);
		action.setLabel("Default Label");
		action.setDescription("This is my description");
		action.setModule("Awesome module!");
		action.setKlass("Class of 2016");
		action.setURL("www.google.com");
		action.setMethod("myMethod");
		String json = plugin.jsonFromObject(action);
		Action object = (Action)plugin.objectFromJson(json, BaseAction.class);
		String classJson = "{ \"Action\" : " + json + " }";
		Object classObject = plugin.objectFromJson(classJson);


		System.out.println(json);
		System.out.println(object);
		System.out.println(classObject);
	}
}
