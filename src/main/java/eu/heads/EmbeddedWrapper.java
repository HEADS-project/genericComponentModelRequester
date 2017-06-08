package eu.heads;

import org.kevoree.ContainerRoot;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Input;
import org.kevoree.annotation.KevoreeInject;
import org.kevoree.api.*;
import org.kevoree.factory.DefaultKevoreeFactory;
import org.kevoree.factory.KevoreeFactory;
import org.kevoree.log.Log;
import org.kevoree.modeling.api.KMFContainer;
import org.kevoree.modeling.api.ModelCloner;
import org.kevoree.modeling.api.compare.ModelCompare;
import org.kevoree.modeling.api.json.JSONModelLoader;
import org.kevoree.modeling.api.trace.TraceSequence;
import org.kevoree.service.ModelService;

@ComponentType(version=1, description=
"This component is able to modify the current Kevoree model when messages are received on its input ports:"+
"<ul>"+
        "<li>modelUpdate</li>"+
        "<li>instancesUpdate</li>"+
"</ul>"+
"<br/>"+
"The messages must respectively be of type:"+
"<ul>"+
        "<li>modelUpdate: <em>a Kevoree JSON model</em></li>"+
        "<li>instancesUpdate: <em>a Kevoree KevScript</em></li>"+
"</ul>")
public class EmbeddedWrapper {

	@KevoreeInject
	private Context context;

	@KevoreeInject
	private ModelService service;

    private final KevoreeFactory fact = new DefaultKevoreeFactory();
    private final JSONModelLoader loader = fact.createJSONLoader();
    private final ModelCompare merge = fact.createModelCompare();
    private final ModelCloner cloner = fact.createModelCloner();

	@Input
    public void modelUpdate(String modelStr) {
        // merge the received model with the current one
        try {
            KMFContainer embeddedModel = loader.loadModelFromString(modelStr).get(0);
            ContainerRoot currentModel = cloner.clone(service.getCurrentModel());
            TraceSequence s = merge.merge(currentModel, embeddedModel);
            if (!s.getTraces().isEmpty()) {
                s.applyOn(currentModel);
                service.update(currentModel, (e) -> {
                    if (e == null) {
                        Log.info("EmbeddedWrapper: model updated successfully");
                    } else {
                        Log.warn("EmbeddedWrapper: model update failed");
                    }
                });
            }
        } catch (Exception e) {
            Log.warn("EmbeddedWrapper: input model is invalid", e);
        }
	}

    @Input
    public void instancesUpdate(String kevscript) {
        // execute the received KevScript in order to add instances to the current model
        service.submitScript(kevscript, (e) -> {
            if (e == null) {
                Log.info("EmbeddedWrapper: KevScript executed successfully");
            } else {
                Log.warn("EmbeddedWrapper: KevScript execution failed");
            }
        });
    }
}
