package  com.transys.core.tags;

//import com.transys.model.StaticData;
//import com.transys.web.SpringAppContext;

//import net.sf.ehcache.Cache;
//import net.sf.ehcache.Element;


public class StaticDataUtil {

	public static String getText(String dataType, String dataValue) {
		/*Cache cache = (Cache)SpringAppContext.getBean("staticDataCache");
		Element element = cache.get(dataType+"_"+dataValue);
		if(element != null)
		{
			StaticData staticData=(StaticData)element.getValue();
			return staticData.getDataText();
		}
		else {
			return dataValue;
		}*/
		return null;
	}
}
