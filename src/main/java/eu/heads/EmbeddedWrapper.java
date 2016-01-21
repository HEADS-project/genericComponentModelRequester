package eu.heads;

import org.kevoree.ContainerRoot;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Input;
import org.kevoree.annotation.KevoreeInject;
import org.kevoree.annotation.Output;
import org.kevoree.api.*;
import org.kevoree.api.handler.UpdateCallback;
import org.kevoree.factory.DefaultKevoreeFactory;
import org.kevoree.factory.KevoreeFactory;
import org.kevoree.log.Log;
import org.kevoree.pmodeling.api.KMFContainer;
import org.kevoree.pmodeling.api.ModelCloner;
import org.kevoree.pmodeling.api.compare.ModelCompare;
import org.kevoree.pmodeling.api.json.JSONModelLoader;
import org.kevoree.pmodeling.api.trace.TraceSequence;

@ComponentType(description="This component is able to modify the current Kevoree model when messages are received on its input ports:"+
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

	@Input
    public void modelUpdate(String modelStr) {
        // merge the received model with the current one
        final KevoreeFactory fact = new DefaultKevoreeFactory();
        final JSONModelLoader loader = new JSONModelLoader(fact);
        final ModelCompare merge = new ModelCompare(fact);
        final ModelCloner cloner = new ModelCloner(fact);

        KMFContainer embeddedModel = loader.loadModelFromString(modelStr).get(0);
        ContainerRoot currentModel = cloner.clone(service.getCurrentModel().getModel());
        TraceSequence s = merge.merge(currentModel, embeddedModel);
        if (!s.getTraces().isEmpty()) {
            s.applyOn(currentModel);
            service.update(currentModel, new UpdateCallback() {
                public void run(Boolean applied) {
                    Log.info("updateModel - {}", applied);
                }
            });
        }
	}

    @Input
    public void instancesUpdate(String kevscript) {
        // execute the received KevScript in order to add instances to the current model
        service.submitScript(kevscript, new UpdateCallback() {
            @Override
            public void run(Boolean applied) {
                Log.info("updateModel - {}", applied);
            }
        });
    }
}
