package net.scientifichooliganism.jsonplugin;

import net.scientifichooliganism.javaplug.interfaces.Action;
import net.scientifichooliganism.javaplug.interfaces.MetaData;
import net.scientifichooliganism.javaplug.interfaces.Task;
import net.scientifichooliganism.javaplug.vo.BaseAction;
import net.scientifichooliganism.javaplug.vo.BaseMetaData;
import net.scientifichooliganism.javaplug.vo.BaseTask;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

public class JSONPluginTest {
    private JSONPlugin plugin;

    public JSONPluginTest () {
        plugin = null;
    }

    @Before
    public void init () {
        plugin = new JSONPlugin();
    }

    @Test
    public void constructorTest () {
        assertNotNull(plugin);
    }

    @Test
    public void test01 () {
        JSONPlugin plugin = new JSONPlugin();
        MetaData data1 = new BaseMetaData();
        MetaData data2 = new BaseMetaData();
        data1.setKey("key1");
        data2.setKey("key2");
        data1.setValue("value1");
        data2.setValue("value2");

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

        Task testTask = new BaseTask();
        testTask.setID("0");
        testTask.setStartDate(new Date());

        String json = plugin.jsonFromObject(testTask);
        System.out.println(json);

        json = "{\"task\":{\"name\":\"test for dateeeeeeeee\",\"startDate\":\"2016-08-10T05:00:00+-0500\",\"completedDate\":\"2016-08-11T05:00:00+-0500\",\"metadata\":[]}}";


        Task object = (Task)plugin.objectFromJson(json);


        System.out.println(json);
        System.out.println(object);
    }
}
