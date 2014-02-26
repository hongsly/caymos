/*
 This file is part of CayMos. 

 CayMos is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 CayMos is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ccs;

import java.util.ArrayList;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import ccs.graph.ComponentRealizationsSampler;
import ccs.graph.ConnectedComponent;
import ccs.graph.ContinuousMotion;
import ccs.graph.ContinuousMotionSamples;
import ccs.graph.Graph;
import ccs.graph.NodeSampler;
import ccs.graph.SamplePoint;
import ccs.graph.SolutionType;

/**
 * Contains info about currently generated continuous motion
 * 
 */
public class MotionModel implements ChangeListener {
	private static MotionModel me = new MotionModel();

	private MotionModel() {
		sampleModel = new PercentageListModel<SamplePoint<Graph>>();
		sModel1 = new PercentageListModel<SamplePoint<Graph>>();
		sModel2 = new PercentageListModel<SamplePoint<Graph>>();

		components = new ArrayList<ConnectedComponent>();
		componentSpinnerModel = new PercentageListModel<ConnectedComponent>();
	}

	public static MotionModel getInstance() {
		return me;
	}

	/**
	 * Current generated continuous motion
	 */
	private ContinuousMotion motion;

	public ContinuousMotion getMotion() {
		return motion;
	}

	/**
	 * Generate two components when there is no path
	 */
	private ContinuousMotion m1, m2;

	public ContinuousMotion getM1() {
		return m1;
	}

	public ContinuousMotion getM2() {
		return m2;
	}

	/**
	 * Samples along the path. Should change when path / cycle generated.
	 */
	private ContinuousMotionSamples<Graph> pSamples;

	public ContinuousMotionSamples<Graph> getMotionSamples() {
		return pSamples;
	}

	/**
	 * Samples from each component. Should change when path / cycle generated.
	 */
	private ContinuousMotionSamples<Graph> samples1, samples2;

	public ContinuousMotionSamples<Graph> getMotionSamples1() {
		return samples1;
	}

	public ContinuousMotionSamples<Graph> getMotionSamples2() {
		return samples2;
	}

	/**
	 * Stores the current value. To be used with corresponding spinner.
	 */
	private PercentageListModel<SamplePoint<Graph>> sampleModel;

	public PercentageListModel<SamplePoint<Graph>> getSpinnerModel() {
		return sampleModel;
	}

	/**
	 * Stores the current value of each component. To be used with corresponding
	 * spinner.
	 */
	private PercentageListModel<SamplePoint<Graph>> sModel1, sModel2;

	public PercentageListModel<SamplePoint<Graph>> getSpinnerModel1() {
		return sModel1;
	}

	public PercentageListModel<SamplePoint<Graph>> getSpinnerModel2() {
		return sModel2;
	}

	private Double startCayleyConfig, endCayleyConfig;
	private Graph startRealization, endRealization;

	public void clear() {
		startCayleyConfig = endCayleyConfig = null;
		startRealization = endRealization = null;
		nearestG1 = nearestG2 = null;
		components = null;
	}

	public Double getStartCayleyConfig() {
		return startCayleyConfig;
	}

	public void setStartCayleyConfig(Double startCayleyConfig) {
		this.startCayleyConfig = startCayleyConfig;
	}

	public Double getEndCayleyConfig() {
		return endCayleyConfig;
	}

	public void setEndCayleyConfig(Double endCayleyConfig) {
		this.endCayleyConfig = endCayleyConfig;
	}

	public Graph getStartRealization() {
		return startRealization;
	}

	public void setStartRealization(Graph startRealization) {
		this.startRealization = startRealization;
	}

	public Graph getEndRealization() {
		return endRealization;
	}

	public void setEndRealization(Graph endRealization) {
		this.endRealization = endRealization;
	}

	public void genComponent() {
		this.refreshComponents();

		TreeDecompModel tdModel = TreeDecompModel.getInstance();

		// TODO: change to find current realization in the list of components???
		// Or define equal for ContinuousMotion?
		ConnectedComponent comp = tdModel.genComponent();
		//TODO: why??? assert(components.contains(comp));
		setComponent(comp);
		/*
		 * motion = tdModel.genComponent(); NodeSampler<Graph> s = new
		 * ComponentRealizationsSampler( tdModel.getTd(), 10); pSamples =
		 * motion.sample(s); sampleModel.setList(pSamples);
		 */

	}

	/**
	 * Set current component (from the list of all components).
	 */
	private void setComponent(ConnectedComponent comp) {

		motion = comp;
		NodeSampler<Graph> s = new ComponentRealizationsSampler(TreeDecompModel
				.getInstance().getTd(), 10);
		pSamples = motion.sample(s);
		sampleModel.setList(pSamples);

		
		// TODO: this.getComponentSpinnerModel().setValue(comp);

		CCSModel.getInstance().clearTypes();
		for (SolutionType t : motion.getSolutionTypes()) {
			CCSModel.getInstance().addType(t);
		}
	}

	/**
	 * @return whether a path is found
	 */
	public boolean genPath() {
		TreeDecompModel tdModel = TreeDecompModel.getInstance();
		if (ControlPanel.getInstance().getPathType() == 2) {
			motion = tdModel.findPath(startRealization, endRealization);
		} else
			motion = tdModel.findPath(startCayleyConfig, endCayleyConfig);

		// Create samples if found a path
		if (motion != null) {
			NodeSampler<Graph> s = new ComponentRealizationsSampler(
					tdModel.getTd(), 10);
			pSamples = motion.sample(s);
			sampleModel.setList(pSamples);

			for (SolutionType t : motion.getSolutionTypes()) {
				CCSModel.getInstance().addType(t);
			}
			return true;
		}

		return false;
	}

	/**
	 * Generate components from start & end when no path exists.
	 */
	public void gen2Components() {
		// TODO: currently only support path between realizations
		assert (ControlPanel.getInstance().getPathType() == 2);

		TreeDecompModel tdModel = TreeDecompModel.getInstance();
		m1 = tdModel.genComponent(getStartRealization());
		NodeSampler<Graph> s1 = new ComponentRealizationsSampler(
				tdModel.getTd(), 10);
		samples1 = m1.sample(s1);
		sModel1.setList(samples1);
		/*
		 * for (SolutionType t : m1.getSolutionTypes()) {
		 * CCSModel.getInstance().addType(t); }
		 */

		m2 = tdModel.genComponent(getEndRealization());
		NodeSampler<Graph> s2 = new ComponentRealizationsSampler(
				tdModel.getTd(), 10);
		samples2 = m2.sample(s2);
		sModel2.setList(samples2);
		/*
		 * for (SolutionType t : m2.getSolutionTypes()) {
		 * CCSModel.getInstance().addType(t); }
		 */
	}

	/**
	 * the nearest two realizations from the 2 components generated resp.
	 */
	private Graph nearestG1, nearestG2;

	/**
	 * @return the realization on component 1 which is nearest to component 2
	 */
	public Graph getNearestG1() {
		if (nearestG1 == null)
			findNearest();
		return nearestG1;
	}

	/**
	 * @return the realization on component 2 which is nearest to component 1
	 */
	public Graph getNearestG2() {
		if (nearestG2 == null)
			findNearest();
		return nearestG2;
	}

	/**
	 * Compute the nearest two realizations from the 2 components generated
	 */
	private void findNearest() {
		double nearestDistance = Double.POSITIVE_INFINITY;
		Graph g1 = null, g2 = null;
		for (SamplePoint<Graph> p1 : samples1) {
			for (SamplePoint<Graph> p2 : samples2) {
				double dis = TreeDecompModel.getInstance().getTd()
						.completeDistance(p1.getValue(), p2.getValue());
				if (dis < nearestDistance) {
					g1 = p1.getValue();
					g2 = p2.getValue();
					nearestDistance = dis;
				}
			}
		}
		nearestG1 = g1;
		nearestG2 = g2;
	}

	/**
	 * TODO: list of all connected components
	 */
	private ArrayList<ConnectedComponent> components;

	// public ArrayList<ContinuousMotion> getComponents() {
	// if (components == null)
	// genComponents();
	// return components;
	// }

	public void refreshComponents() {
		if (components == null)
			genComponents();
		Debug.warnMsg("total # of components: "+ components.size());
		Debug.warnMsg("#o:"+TreeDecompModel.getInstance().getTd().ccs.getNumOfSolutionTypes());
	}

	private void genComponents() {
		components = ConnectedComponent.findAllComponents(TreeDecompModel
				.getInstance().getTd());
		componentSpinnerModel.setList(components);
	}

	/**
	 * Stores the current component. To be used with corresponding spinner.
	 */
	private PercentageListModel<ConnectedComponent> componentSpinnerModel;

	public PercentageListModel<ConnectedComponent> getComponentSpinnerModel() {
		return componentSpinnerModel;
	}

	public void stateChanged(ChangeEvent arg0) {
		// TODO Auto-generated method stub
		// change current realization??? update connected component curve???
		ConnectedComponent comp = getComponentSpinnerModel().getValue();

		setComponent(comp);

		// pathSpinner.fire
		// GPanel,
		CCSPanel.getInstance().repaint();

		// TODO: set control.componentSpinner to current component in
		// setComponent()

	}
}
