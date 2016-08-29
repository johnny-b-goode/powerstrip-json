package net.scientifichooliganism.jsonplugin;

import net.scientifichooliganism.javaplug.interfaces.Action;
import net.scientifichooliganism.javaplug.interfaces.MetaData;
import net.scientifichooliganism.javaplug.interfaces.Task;
import net.scientifichooliganism.javaplug.vo.BaseAction;
import net.scientifichooliganism.javaplug.vo.BaseMetaData;
import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;

public class JSONPluginTest {
    private JSONPlugin plugin;
    private DateFormat sdf;

    public JSONPluginTest () {
        plugin = null;
        sdf = null;
    }

    @Before
    public void init () {
        plugin = new JSONPlugin();
        sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+Z");
    }

    @Test
    public void constructorTest () {
        assertNotNull(plugin);
    }

    @Test
    public void jsonFromObejctTest01 () {
        String metaDataKey = "some_key";
        String metaDataValue = "some_value";
        String metaDataID = "12";
        String metaDataObject = "some_object";
        String metaDataObjectID = "254";
        String metaDataLabel = "some_label";

        MetaData data1 = new BaseMetaData();
        data1.setKey(metaDataKey);
        data1.setValue(metaDataValue);
        data1.setID(metaDataID);
        data1.setObject(metaDataObject);
        data1.setObjectID(metaDataObjectID);
        data1.setLabel(metaDataLabel);

        String actionId = String.valueOf(42);
        String actionLabel = "some_label";
        String actionDescription = "some_description";
        String actionModule = "some_module";
        String actionClass = "some_class";
        String actionURL = "some_url";
        String actionMethod = "some_method";

        Action action = new BaseAction();
        action.setID(actionId);
        action.setLabel(actionLabel);
        action.setDescription(actionDescription);
        action.setModule(actionModule);
        action.setKlass(actionClass);
        action.setURL(actionURL);
        action.setMethod(actionMethod);
        action.addMetaData(data1);

        String expectedResult = "{\"action\":" +
                "{\"description\":\"" + actionDescription + "\"," +
                "\"module\":\"" + actionModule + "\"," +
                "\"class\":\"" + actionClass + "\"," +
                "\"url\":\"" + actionURL + "\"," +
                "\"method\":\"" + actionMethod + "\"," +
                "\"id\":\"" + actionId + "\"," +
                "\"label\":\"" + actionLabel + "\"," +
                "\"metadata\":" +
                "[" +
                "{\"object\":\"" + metaDataObject + "\"," +
                "\"object_id\":\"" + metaDataObjectID + "\"," +
                "\"key\":\"" + metaDataKey + "\"," +
                "\"value\":\"" + metaDataValue + "\"," +
                "\"id\":\"" + metaDataID + "\"," +
                "\"label\":\"" + metaDataLabel + "\"}" +
                "]" +
                "}}";

        assertEquals(expectedResult, plugin.jsonFromObject(action));

    }

    @Test
    public void objectFromJsonTest02(){
        String taskName = "some_name";
        String taskStartDateString = "2016-08-10T05:00:00+-0500";
        Date taskStartDate = null;
        String taskCompletedDateString = "2016-08-11T05:00:00+-0500";
        Date taskCompletedDate = null;
        String json = "{\"task\":" +
                "{\"name\":\"" + taskName + "\"," +
                "\"start_date\":\"" + taskStartDateString + "\"," +
                "\"completed_date\":\"" + taskCompletedDateString + "\"," +
                "\"metadata\":[]}}";

        try {
            taskStartDate = sdf.parse(taskStartDateString);
            taskCompletedDate = sdf.parse(taskCompletedDateString);
        } catch (Exception exc){
            exc.printStackTrace();
            fail("Error parsing date strings!\n" +
                    "Exception: " + exc.getClass().getSimpleName() + "\n" +
                    "Message: " + exc.getMessage());
        }

        Task object = (Task)plugin.objectFromJson(json);

        assertEquals(taskName, object.getName());
        assertEquals(taskStartDate, object.getStartDate());
        assertEquals(taskCompletedDate, object.getCompletedDate());
        assert(object.getMetaData().isEmpty());
    }
}
