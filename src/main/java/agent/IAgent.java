package agent;

import agent.internal.IDriverActions;
import agent.internal.IWindowActions;

public interface IAgent extends IWindowActions, IDriverActions {
	void takeConditionalSnapShot() throws Exception;
}