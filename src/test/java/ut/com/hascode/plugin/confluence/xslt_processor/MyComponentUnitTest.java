package ut.com.hascode.plugin.confluence.xslt_processor;

import org.junit.Test;
import com.hascode.plugin.confluence.xslt_processor.MyPluginComponent;
import com.hascode.plugin.confluence.xslt_processor.MyPluginComponentImpl;

import static org.junit.Assert.assertEquals;

public class MyComponentUnitTest
{
    @Test
    public void testMyName()
    {
        MyPluginComponent component = new MyPluginComponentImpl(null);
        assertEquals("names do not match!", "myComponent",component.getName());
    }
}