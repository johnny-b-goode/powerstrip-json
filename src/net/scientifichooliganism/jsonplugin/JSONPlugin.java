package net.scientifichooliganism.jsonplugin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.scientifichooliganism.javaplug.interfaces.*;
import net.scientifichooliganism.javaplug.vo.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONPlugin implements Plugin {
	static final String BASE_CLASS_NAME = "net.scientifichooliganism.javaplug.vo.Base";
	Pattern classPattern = Pattern.compile("\\w+");
	Pattern classStripPattern = Pattern.compile("\\{[^{}]+\\}");
	Gson gson;

	JSONPlugin () {
		gson = new GsonBuilder().setPrettyPrinting()
				.setFieldNamingStrategy(new JavaPlugFieldNamingStrategy())
				.create();
	}

	@Override
	public String[][] getActions() {
		return new String[0][];
	}

	public <T extends ValueObject> String jsonFromObject(T object){
		return gson.toJson(object);
	}

	public <T extends ValueObject> T objectFromJson(String json){
		return objectFromJson(stripClass(json), classFromJson(json));
	}

	public <T> T objectFromJson(String json, Class klass){
		// Equate interfaces with class types
		if(klass == Action.class){
			klass = BaseAction.class;
		} else if (klass == Application.class) {
			klass = BaseApplication.class;
		} else if (klass == Block.class) {
			klass = BaseBlock.class;
		} else if (klass == Configuration.class) {
			klass = BaseConfiguration.class;
		} else if (klass == Environment.class) {
			klass = BaseEnvironment.class;
		} else if (klass == Event.class) {
			klass = BaseEvent.class;
		} else if (klass == Release.class) {
			klass = BaseRelease.class;
		} else if (klass == Task.class) {
			klass = BaseTask.class;
		} else if (klass == TaskCategory.class) {
			klass = BaseTaskCategory.class;
		} else if (klass == Transaction.class) {
			klass = BaseTransaction.class;
		} else if (klass == ValueObject.class) {
			klass = BaseValueObject.class;
		}

		return (T)gson.fromJson(json, klass);
	}

	private String stripClass(String json){
		Matcher matcher = classStripPattern.matcher(json);
		if(matcher.find()){
			return matcher.group();
		} else {
			throw new RuntimeException("Ill-formatted JSON");
		}
	}

	private Class classFromJson(String json){
		Matcher matcher = classPattern.matcher(json);
		String className = null;
		Class ret = null;
		if(matcher.find()){
			className = matcher.group();
		}
		if(className != null){
			try {
				ret = Class.forName(BASE_CLASS_NAME + className);
			}
			catch (Exception exc){
				System.out.println("Class specified (" + className + ") not found!");
				exc.printStackTrace();
			}
		}

		return ret;
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
		Action object = plugin.objectFromJson(json, Action.class);
		String classJson = "{ \"Action\" : " + json + " }";
		Action classObject = plugin.objectFromJson(classJson);
		System.out.println(json);
		System.out.println(object);
		System.out.println(classObject);
	}
}
