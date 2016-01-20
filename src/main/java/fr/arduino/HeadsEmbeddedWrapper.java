package fr.arduino;

import java.util.List;

import org.kevoree.ContainerRoot;
import org.kevoree.annotation.ComponentType;
import org.kevoree.annotation.Input;
import org.kevoree.annotation.KevoreeInject;
import org.kevoree.annotation.Output;
import org.kevoree.annotation.Param;
import org.kevoree.annotation.Start;
import org.kevoree.annotation.Stop;
import org.kevoree.annotation.Update;
import org.kevoree.api.Callback;
import org.kevoree.api.CallbackResult;
import org.kevoree.api.ModelService;
import org.kevoree.api.Port;
import org.kevoree.api.handler.ModelListener;
import org.kevoree.api.handler.UpdateCallback;
import org.kevoree.api.handler.UpdateContext;
import org.kevoree.factory.DefaultKevoreeFactory;
import org.kevoree.factory.KevoreeFactory;
import org.kevoree.pmodeling.api.KMFContainer;
import org.kevoree.pmodeling.api.ModelCloner;
import org.kevoree.pmodeling.api.compare.ModelCompare;
import org.kevoree.pmodeling.api.json.JSONModelLoader;
import org.kevoree.pmodeling.api.json.JSONModelSerializer;
import org.kevoree.pmodeling.api.trace.TraceSequence;

@ComponentType(description="This component cas be added to a Java Node. It will trigger the getModel and getInstance port after receiving a new Model")
public class HeadsEmbeddedWrapper implements ModelListener {

	@KevoreeInject
	org.kevoree.api.Context context;

	@KevoreeInject
	ModelService service;

	@Param(defaultValue = "true")
	boolean test = true;

	@Start
	public void start() {
		service.registerModelListener(this);
	}
	
	ContainerRoot r;

	
	@Output(optional=true)
	Port getModel;

	@Output(optional=true)
	Port getModelInstance;

	
	@Input
	void triggerEmbeeddedSystem(){
		
		if (!getArduinoModel) {
			getModel.send("getModel", new Callback<String>() {
				public void onError(Throwable arg0) {}
				public void onSuccess(CallbackResult arg0) {
					getArduinoModel = true;
					getArduinoModelInstance = false;
					final KevoreeFactory fact = new DefaultKevoreeFactory();
					final JSONModelLoader loader = new JSONModelLoader(fact);
					List<KMFContainer> list0 = loader.loadModelFromString(arg0.getPayload());
					ModelCompare merge = new ModelCompare(fact);
					ModelCloner cloner = fact.createModelCloner();
					ContainerRoot r1 = cloner.clone(r);
					TraceSequence s = merge.merge(r1, list0.get(0));
					s.applyOn(r1);
					service.update(r1, new UpdateCallback() {

						public void run(Boolean arg0) {
							System.err.println("ok");
						}
					});
				
				
				}
				
			});
		}

		
	}
	
	@Stop
	public void stop() {
		service.unregisterModelListener(this);
	}

	@Update
	public void update() {
	}

	public boolean afterLocalUpdate(UpdateContext arg0) {
		r = arg0.getProposedModel();
		return true;
	}

	public boolean initUpdate(UpdateContext arg0) {
		return true;
	}

	boolean getArduinoModel = false;
	boolean getArduinoModelInstance = false;

	public void modelUpdated() {
		if (test) {
			if (!getArduinoModel) {
				final KevoreeFactory fact = new DefaultKevoreeFactory();
				
				JSONModelLoader loader = new JSONModelLoader(fact);
				List<KMFContainer> list = loader.loadModelFromString("{     \"class\": \"root:org.kevoree.ContainerRoot@0.76609817403368651453298639450\",     \"generated_KMF_ID\": \"0.76609817403368651453298639450\",     \"nodes\": [],     \"repositories\": [],     \"hubs\": [],     \"mBindings\": [],     \"groups\": [],     \"packages\": [         {             \"class\": \"org.kevoree.Package@eu\",             \"name\": \"eu\",             \"packages\": [                 {                     \"class\": \"org.kevoree.Package@heads\",                     \"name\": \"heads\",                     \"packages\": [],                     \"typeDefinitions\": [                         {                             \"class\": \"org.kevoree.NodeType@name=ArduinoNode,version=1.0.0\",                             \"abstract\": \"false\",                             \"name\": \"ArduinoNode\",                             \"version\": \"1.0.0\",                             \"deployUnits\": [],                             \"superTypes\": [],                             \"dictionaryType\": [                                 {                                     \"class\": \"org.kevoree.DictionaryType@0.0101809580810368061453298639480\",                                     \"generated_KMF_ID\": \"0.0101809580810368061453298639480\",                                     \"attributes\": []                                 }                             ],                             \"metaData\": [                                 {                                     \"class\": \"org.kevoree.Value@virtual\",                                     \"name\": \"virtual\",                                     \"value\": \"true\"                                 }                             ]                         },                         {                             \"class\": \"org.kevoree.ComponentType@name=LedComponent,version=1.0.0\",                             \"abstract\": \"false\",                             \"name\": \"LedComponent\",                             \"version\": \"1.0.0\",                             \"deployUnits\": [],                             \"superTypes\": [],                             \"dictionaryType\": [                                 {                                     \"class\": \"org.kevoree.DictionaryType@0.42434838530607521453298414303\",                                     \"generated_KMF_ID\": \"0.42434838530607521453298414303\",                                     \"attributes\": []                                 }                             ],                             \"metaData\": [                                 {                                     \"class\": \"org.kevoree.Value@virtual\",                                     \"name\": \"virtual\",                                     \"value\": \"true\"                                 }                             ],                             \"required\": [                                 {                                     \"class\": \"org.kevoree.PortTypeRef@out\",                                     \"noDependency\": \"false\",                                     \"optional\": \"false\",                                     \"name\": \"out\",                                     \"ref\": [],                                     \"mappings\": []                                 }                             ],                             \"provided\": [                                 {                                     \"class\": \"org.kevoree.PortTypeRef@in\",                                     \"noDependency\": \"false\",                                     \"optional\": \"false\",                                     \"name\": \"in\",                                     \"ref\": [],                                     \"mappings\": []                                 }                             ]                         },                         {                             \"class\": \"org.kevoree.ChannelType@name=VirtualChan,version=1.0.0\",                             \"upperFragments\": \"0\",                             \"abstract\": \"false\",                             \"upperBindings\": \"0\",                             \"lowerBindings\": \"0\",                             \"lowerFragments\": \"0\",                             \"name\": \"VirtualChan\",                             \"version\": \"1.0.0\",                             \"deployUnits\": [],                             \"superTypes\": [],                             \"dictionaryType\": [                                 {                                     \"class\": \"org.kevoree.DictionaryType@0.43144291522912681453298470489\",                                     \"generated_KMF_ID\": \"0.43144291522912681453298470489\",                                     \"attributes\": []                                 }                             ],                             \"metaData\": [                                 {                                     \"class\": \"org.kevoree.Value@virtual\",                                     \"name\": \"virtual\",                                     \"value\": \"true\"                                 }                             ]                         }                     ],                     \"deployUnits\": []                 }             ],             \"typeDefinitions\": [],             \"deployUnits\": []         }     ] }");
				ModelCompare merge = new ModelCompare(fact);
				ModelCloner cloner = fact.createModelCloner();
				ContainerRoot r1 = cloner.clone(r);
				TraceSequence s = merge.merge(r1, list.get(0));
				s.applyOn(r1);

				getArduinoModel = true;
				service.update(r1, new UpdateCallback() {

					public void run(Boolean arg0) {

					}
				});
			}
			if (getArduinoModel && !getArduinoModelInstance) {
				getArduinoModelInstance = true;
				service.submitScript(
						"add node938 : eu.heads.ArduinoNode\n" + "add node938.comp772 : eu.heads.LedComponent\n"
								+ "add chan727 : eu.heads.VirtualChan\n"
								+ "bind node938.comp772.in chan727\n" + "bind node0.com1.triggerEmbeeddedSystem chan727",
						new UpdateCallback() {
							public void run(Boolean arg0) {
								// System.err.println(arg0);
							}
						});
			}
		}
		else{
			if (!getArduinoModel) {
				getModel.send("getModel", new Callback<String>() {

					public void onError(Throwable arg0) {
						
					}

					public void onSuccess(CallbackResult arg0) {
						getArduinoModel = true;
						getArduinoModelInstance = false;
						final KevoreeFactory fact = new DefaultKevoreeFactory();
						final JSONModelLoader loader = new JSONModelLoader(fact);
						List<KMFContainer> list0 = loader.loadModelFromString(arg0.getPayload());
						ModelCompare merge = new ModelCompare(fact);
						ModelCloner cloner = fact.createModelCloner();
						ContainerRoot r1 = cloner.clone(r);
						TraceSequence s = merge.merge(r1, list0.get(0));
						s.applyOn(r1);
						service.update(r1, new UpdateCallback() {

							public void run(Boolean arg0) {
								System.err.println("ok");
							}
						});
					
					
					}
					
				});
			}
				if (getArduinoModel && !getArduinoModelInstance) {
					getModelInstance.send("getInstance", new  Callback<String>() {

						public void onError(Throwable arg0) {
							// TODO Auto-generated method stub
							
						}

						public void onSuccess(CallbackResult arg0) {
							getArduinoModelInstance = true;
							getArduinoModel = false;
							service.submitScript(arg0.getPayload(),
									new UpdateCallback() {
										public void run(Boolean arg0) {
											// System.err.println(arg0);
										}
									});
							
						}
					});
			}

		}

	}

	public void postRollback(UpdateContext arg0) {
	}

	public void preRollback(UpdateContext arg0) {
	}


	public boolean preUpdate(UpdateContext arg0) {
		return true;
	}

}
