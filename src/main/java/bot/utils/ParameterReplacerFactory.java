package bot.utils;

import bot.utils.replacers.IParameterReplacer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import java.util.Map;

@Component
public class ParameterReplacerFactory {

    private Map<String, IParameterReplacer> replacerMap;

    @Autowired
    public void setReplacers(IParameterReplacer[] parameterReplacersList){
        this.replacerMap = getParamReplacingMap(parameterReplacersList);
    }

    public IParameterReplacer getReplacer(String param){
        return this.replacerMap.get(param);
    }

    private Map<String, IParameterReplacer> getParamReplacingMap(IParameterReplacer[] paramReplacers){
        Map<String, IParameterReplacer>  map = new HashMap<>();
        for (var replacer : paramReplacers){
            map.put(replacer.getHandleParameter(), replacer);
        }
        return map;
    }
}
