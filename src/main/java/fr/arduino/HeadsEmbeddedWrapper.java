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

	
	@Output(optional=false)
	Port getModel;

	@Output(optional=false)
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
				List<KMFContainer> list = loader.loadModelFromString(
						"{\"class\":\"org.kevoree.ContainerRoot@0.58330028364434841449655951491\",\"generated_KMF_ID\":\"0.58330028364434841449655951491\",\"nodes\":[],\"repositories\":[]"
								+ ",\"hubs\":[],\"mBindings\":[],\"groups\":[],\"packages\":["
								+ "{\"class\":\"org.kevoree.Package@eu\",\"name\":\"eu\",\"packages\":["
								+ "{\"class\":\"org.kevoree.Package@heads\",\"name\":\"heads\",\"packages\":[],\"typeDefinitions\":["
								+ "{\"class\":\"org.kevoree.ComponentType@name=ArduinoDigitalInput,version=1.0.0\",\"abstract\":\"false\",\"name\":\"ArduinoDigitalInput\",\"version\":\"1.0.0\",\"deployU"
								+ "nits\":[\"packages[eu]/packages[heads]/deployUnits[hashcode=,name=ArduinoDigitalInput,version=1.0.0]\"],\"superTypes\":[],\"dictionaryType\":["
								+ "{\"class\":\"org.kevoree.DictionaryType@0.55247031222097581449655951510\",\"generated_KMF_ID\":\"0.55247031222097581449655951510\",\"attributes\":["
								+ "{\"class\":\"org.kevoree.DictionaryAttribute@pin\",\"fragmentDependant\":\"false\",\"optional\":\"true\",\"name\":\"pin\",\"state\":\"false\",\"datatype\":\"INT\",\"defaultVal"
								+ "ue\":\"13\",\"genericTypes\":[]}" + "]}"
								+ "],\"metaData\":[],\"required\":[],\"provided\":["
								+ "{\"class\":\"org.kevoree.PortTypeRef@off\",\"noDependency\":\"false\",\"optional\":\"false\",\"name\":\"off\",\"ref\":[],\"mappings\":[]}"
								+ ","
								+ "{\"class\":\"org.kevoree.PortTypeRef@on\",\"noDependency\":\"false\",\"optional\":\"false\",\"name\":\"on\",\"ref\":[],\"mappings\":[]}"
								+ "]}" + "],\"deployUnits\":["
								+ "{\"class\":\"org.kevoree.DeployUnit@hashcode=,name=ArduinoDigitalInput,version=1.0.0\",\"name\":\"ArduinoDigitalInput\",\"hashcode\":\"\",\"url\":\"\",\"version\":\"1.0."
								+ "0\",\"requiredLibs\":[],\"filters\":["
								+ "{\"class\":\"org.kevoree.Value@platform\",\"name\":\"platform\",\"value\":\"arduino\"}"
								+ "]}" + "]}" + "],\"typeDefinitions\":[],\"deployUnits\":[]}" + "]}");

				String nodemode = "{\"class\":\"org.kevoree.ContainerRoot@0.71788802114315331449658269020\",\"generated_KMF_ID\":\"0.71788802114315331449658269020\",\"nodes\":[],\"repositories\":[],\"hubs\":[],\"mBindings\":[],\"groups\":[],\"packages\":[{\"class\":\"org.kevoree.Package@eu\",\"name\":\"eu\",\"packages\":[{\"class\":\"org.kevoree.Package@heads\",\"name\":\"heads\",\"packages\":[],\"typeDefinitions\":[{\"class\":\"org.kevoree.NodeType@name=ArduinoNode,version=1.0.0\",\"abstract\":\"false\",\"name\":\"ArduinoNode\",\"version\":\"1.0.0\",\"deployUnits\":[\"packages[eu]/packages[heads]/deployUnits[hashcode=,name=ArduinoNode,version=1.0.0]\"],\"superTypes\":[],\"dictionaryType\":[{\"class\":\"org.kevoree.DictionaryType@0.49977423623204231449658269047\",\"generated_KMF_ID\":\"0.49977423623204231449658269047\",\"attributes\":[{\"class\":\"org.kevoree.DictionaryAttribute@serialPort\",\"fragmentDependant\":\"false\",\"optional\":\"true\",\"name\":\"serialPort\",\"state\":\"false\",\"datatype\":\"STRING\",\"defaultValue\":\"/dev/serial\",\"genericTypes\":[]}]}],\"metaData\":[]}],\"deployUnits\":[{\"class\":\"org.kevoree.DeployUnit@hashcode=,name=ArduinoNode,version=1.0.0\",\"name\":\"ArduinoNode\",\"hashcode\":\"\",\"url\":\"\",\"version\":\"1.0.0\",\"requiredLibs\":[],\"filters\":[{\"class\":\"org.kevoree.Value@platform\",\"name\":\"platform\",\"value\":\"arduino\"}]}]}],\"typeDefinitions\":[],\"deployUnits\":[]}]}";
				List<KMFContainer> list1 = loader.loadModelFromString(nodemode);

				String channelName = "{\"class\":\"org.kevoree.ContainerRoot@0.81874583777971571449663371208\",\"generated_KMF_ID\":\"0.81874583777971571449663371208\",\"nodes\":[],\"repositories\":[],\"hubs\":[],\"mBindings\":[],\"groups\":[],\"packages\":[{\"class\":\"org.kevoree.Package@eu\",\"name\":\"eu\",\"packages\":[{\"class\":\"org.kevoree.Package@heads\",\"name\":\"heads\",\"packages\":[],\"typeDefinitions\":[{\"class\":\"org.kevoree.ChannelType@name=ArduinoChannel,version=1.0.0\",\"upperFragments\":\"0\",\"abstract\":\"false\",\"upperBindings\":\"0\",\"lowerBindings\":\"0\",\"lowerFragments\":\"0\",\"name\":\"ArduinoChannel\",\"version\":\"1.0.0\",\"deployUnits\":[\"packages[eu]/packages[heads]/deployUnits[hashcode=,name=ArduinoChannel,version=1.0.0]\"],\"superTypes\":[],\"dictionaryType\":[{\"class\":\"org.kevoree.DictionaryType@0.8498527975752951449663371226\",\"generated_KMF_ID\":\"0.8498527975752951449663371226\",\"attributes\":[]}],\"metaData\":[]}],\"deployUnits\":[{\"class\":\"org.kevoree.DeployUnit@hashcode=,name=ArduinoChannel,version=1.0.0\",\"name\":\"ArduinoChannel\",\"hashcode\":\"\",\"url\":\"\",\"version\":\"1.0.0\",\"requiredLibs\":[],\"filters\":[{\"class\":\"org.kevoree.Value@platform\",\"name\":\"platform\",\"value\":\"arduino\"}]}]}],\"typeDefinitions\":[],\"deployUnits\":[]}]}";
				List<KMFContainer> list2 = loader.loadModelFromString(channelName);

				ModelCompare merge = new ModelCompare(fact);

				TraceSequence s = merge.merge(list.get(0), list1.get(0));
				// System.err.println(s.toString());
				s.applyOn(list.get(0));
				s = merge.merge(list.get(0), list2.get(0));
				s.applyOn(list.get(0));
				JSONModelSerializer serializer = new JSONModelSerializer();
				System.err.println(serializer.serialize(list.get(0)));
				ModelCloner cloner = fact.createModelCloner();
				ContainerRoot r1 = cloner.clone(r);
				s = merge.merge(r1, list.get(0));
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
						"add node938 : eu.heads.ArduinoNode\n" + "add node938.comp772 : eu.heads.ArduinoDigitalInput\n"
								+ "add chan727, chan883 : eu.heads.ArduinoChannel\n"
								+ "bind node938.comp772.off chan727\n" + "bind node938.comp772.on chan883",
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
