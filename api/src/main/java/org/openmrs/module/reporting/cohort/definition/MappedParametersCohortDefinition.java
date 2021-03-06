package org.openmrs.module.reporting.cohort.definition;

import org.openmrs.module.reporting.definition.configuration.ConfigurationProperty;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;

import java.util.HashMap;
import java.util.Map;

/**
 * Allows you to easily expose a cohort definition with different names for its parameters (which typically must be the
 * same as @ConfigurationProperty-annotated properties.
 */
public class MappedParametersCohortDefinition extends BaseCohortDefinition {

    @ConfigurationProperty
    private Mapped<CohortDefinition> wrapped;

    public MappedParametersCohortDefinition() {
    }

    /**
     * Example usage:
     * new MappedParametersCohortDefinition(encounterCD, "onOrAfter", "startDate", "onOrBefore", "endDate");
     * @param toWrap
     * @param renamedParameters must have an even number of entries. Each pair should be the is the original parameter
     *                          name, followed by the new parameter name
     */
    public MappedParametersCohortDefinition(CohortDefinition toWrap, String... renamedParameters) {
        this(toWrap, toMap(renamedParameters));
    }

    public MappedParametersCohortDefinition(CohortDefinition toWrap, Map<String, String> renamedParameters) {
        Map<String, Object> mappings = new HashMap<String, Object>();
        for (Map.Entry<String, String> entry : renamedParameters.entrySet()) {
            String originalParameterName = entry.getKey();
            String newParameterName = entry.getValue();

            mappings.put(originalParameterName, "${" + newParameterName + "}");

            Parameter originalParameter = toWrap.getParameter(originalParameterName);
            Parameter newParameter = new Parameter();
            newParameter.setName(newParameterName);
            newParameter.setLabel(originalParameter.getLabel());
            newParameter.setType(originalParameter.getType());
            newParameter.setCollectionType(originalParameter.getCollectionType());
            newParameter.setDefaultValue(originalParameter.getDefaultValue());
            newParameter.setWidgetConfiguration(originalParameter.getWidgetConfiguration());
            addParameter(newParameter);
        }
        wrapped = new Mapped<CohortDefinition>(toWrap, mappings);
    }

    public Mapped<CohortDefinition> getWrapped() {
        return wrapped;
    }

    public void setWrapped(Mapped<CohortDefinition> wrapped) {
        this.wrapped = wrapped;
    }

    private static Map<String, String> toMap(String... keysAndValues) {
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < keysAndValues.length; i += 2) {
            map.put(keysAndValues[i], keysAndValues[i + 1]);
        }
        return map;
    }

}
