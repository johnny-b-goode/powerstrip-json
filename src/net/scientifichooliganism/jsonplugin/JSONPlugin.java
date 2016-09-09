package net.scientifichooliganism.jsonplugin;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.scientifichooliganism.javaplug.annotations.Param;
import net.scientifichooliganism.javaplug.interfaces.*;
import net.scientifichooliganism.javaplug.util.JavaLogger;
import net.scientifichooliganism.javaplug.util.LumberJack;
import net.scientifichooliganism.javaplug.util.SpringBoard;
import net.scientifichooliganism.javaplug.vo.*;

import java.lang.reflect.Type;
import java.util.*;

public class JSONPlugin implements Plugin {
	Gson gson;
    LumberJack logger;

	public JSONPlugin () {
		gson = new GsonBuilder()
				.setFieldNamingStrategy(new JavaPlugFieldNamingStrategy())
				.registerTypeAdapter(MetaData.class, new MetaDataAdapter())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss+Z")
				.create();
        logger = JavaLogger.getInstanceForContext(this.getClass().getName());
	}

	@Override
	public String[][] getActions() {
		return new String[0][];
	}

	public String jsonFromObject(@Param(name="object") Object object){
		String objectName;
		if(ValueObject.class.isAssignableFrom(object.getClass())){
			objectName = stringFromObject((ValueObject)object);
		} else if(Collection.class.isAssignableFrom(object.getClass())) {
			ArrayList<String> jsonStrings = new ArrayList<>();
			for(Object o : (Collection)object){
				jsonStrings.add(jsonFromObject(o));
			}
			String json = "{\"data\":[";
			for(String s : jsonStrings){
				json += s;
				json += ",";
			}
			if(json.charAt(json.length() - 1) == ',') {
				json = json.substring(0, json.length() - 1);
			}
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
			    logger.logException(exc, SpringBoard.ERROR);
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
			return "metadata";
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
			case "metadata":
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
}
