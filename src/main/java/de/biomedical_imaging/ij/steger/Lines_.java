/*
 * #%L
 * Ridge Detection plugin for ImageJ
 * %%
 * Copyright (C) 2014 - 2015 Thorsten Wagner (ImageJ java plugin), 1996-1998 Carsten Steger (original C code), 1999 R. Balasubramanian (detect lines code to incorporate within GRASP)
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-2.0.html>.
 * #L%
 */

package de.biomedical_imaging.ij.steger;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Font;
import java.awt.Polygon;
import java.awt.TextField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ij.CompositeImage;
import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.Prefs;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.gui.Overlay;
import ij.gui.PointRoi;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.gui.TextRoi;
import ij.measure.Calibration;
import ij.measure.ResultsTable;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.plugin.frame.RoiManager;
import ij.process.FloatPolygon;
import ij.process.ImageProcessor;

// TODO: Auto-generated Javadoc
/**
 * The Class Lines_.
 */
public class Lines_ implements ExtendedPlugInFilter, DialogListener {

	/** The Constant lineWidthDefault. */
	final static double lineWidthDefault = 3.5;

	/** The line width. */
	double lineWidth = lineWidthDefault;

	/** The Constant contrastHighDefault. */
	final static double contrastHighDefault = 230;

	/** The contrast high. */
	double contrastHigh = contrastHighDefault;

	/** The Constant contrastLowDefault. */
	final static double contrastLowDefault = 87;

	/** The contrast low. */
	double contrastLow = contrastLowDefault;

	/** The Constant sigmaDefault. */
	final static double sigmaDefault = 1.51;

	/** The sigma. */
	double sigma = sigmaDefault;

	/** The Constant lowerThreshDefault. */
	final static double lowerThreshDefault = 3.06;

	/** The lower thresh. */
	double lowerThresh = lowerThreshDefault;

	/** The Constant upperThreshDefault. */
	final static double upperThreshDefault = 7.99;

	/** The upper thresh. */
	double upperThresh = upperThreshDefault;

	/** The Constant minLengthDefault. */
	final static double minLengthDefault = 0;

	/** The min length. */
	double minLength = minLengthDefault;

	/** The Constant maxLengthDefault. */
	final static double maxLengthDefault = 0;

	/** The max length. */
	double maxLength = maxLengthDefault;

	/** The Constant isDarkLineDefault. */
	final static boolean isDarkLineDefault = false;

	/** The is dark line. */
	boolean isDarkLine = isDarkLineDefault;

	/** The Constant doCorrectPositionDefault. */
	final static boolean doCorrectPositionDefault = false;

	/** The do correct position. */
	boolean doCorrectPosition = doCorrectPositionDefault;

	/** The Constant doEstimateWidthDefault. */
	final static boolean doEstimateWidthDefault = false;

	/** The do estimate width. */
	boolean doEstimateWidth = doEstimateWidthDefault;

	/** The Constant doExtendLineDefault. */
	final static boolean doExtendLineDefault = true;

	/** The do extend line. */
	boolean doExtendLine = doExtendLineDefault;

	/** The Constant showJunctionPointsDefault. */
	final static boolean showJunctionPointsDefault = false;

	/** The show junction points. */
	boolean showJunctionPoints = showJunctionPointsDefault;

	/** The Constant displayResultsDefault. */
	final static boolean displayResultsDefault = true;

	/** The display results. */
	boolean displayResults = displayResultsDefault;

	/** The Constant addToRoiManagerDefault. */
	final static boolean addToRoiManagerDefault = true;

	/** The add to roi manager. */
	boolean addToRoiManager = addToRoiManagerDefault;

	/** The Constant makeBinaryDefault. */
	final static boolean makeBinaryDefault = false;

	/** The make binary. */
	boolean makeBinary = makeBinaryDefault;

<<<<<<< HEAD
	final static boolean doStackDefault = false;
	boolean doStack = doStackDefault;
	
=======
	/** The overlap option. */
>>>>>>> 0f3c0ef1a3ab97d6fd0c143469e5668f82f3d205
	OverlapOption overlapOption = OverlapOption.NONE;

	/** The Constant showIDsDefault. */
	final static boolean showIDsDefault = false;

	/** The show I ds. */
	boolean showIDs = showIDsDefault;

	/** The Constant verboseDefault. */
	final static boolean verboseDefault = false;

	/** The verbose. */
	boolean verbose = verboseDefault;

	/** The is preview. */
	boolean isPreview = false;

	/** The contrast or line width changed once. */
	boolean contrastOrLineWidthChangedOnce = false;
<<<<<<< HEAD
	
=======

	/** The do stack. */
	boolean doStack = false;

	/** The used options. */
>>>>>>> 0f3c0ef1a3ab97d6fd0c143469e5668f82f3d205
	private Options usedOptions = null;

	/** The instance. */
	private static Lines_ instance = null;

	/** For each frame an ArrayList with the lines of a single frame *. *///
	ArrayList<Lines> result;

	/** For each frame an ArrayList with the junctions of a single frame *. */
	ArrayList<Junctions> resultJunction;

	/** The imp. */
	ImagePlus imp;

	/**
	 * Instantiates a new lines.
	 */
	public Lines_() {
		instance = this;
	}

	/**
	 * Access the results from an external plugin.
	 *
	 * @return return instance of Lines_
	 */
	public static Lines_ getInstance() {
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ij.plugin.filter.PlugInFilter#setup(java.lang.String, ij.ImagePlus)
	 */
	@Override
	public int setup(String arg, ImagePlus imp) {
		if (arg.equals("final")) {
			sortLists();
			// assignLinesToJunctions();
			displayContours();
			if (displayResults) {
				createResultsTable(true);
			}
			if (addToRoiManager) {
				addToRoiManager();
			}
			if (makeBinary) {
				makeBinary();
			}
			return DONE;

		}
		// Check if the apache commons lang library is available
		try {
			Class.forName("org.apache.commons.lang3.mutable.MutableLong");
		} catch (ClassNotFoundException exception) {
			IJ.error("Please install apache-commons-lang 3",
					"It seems that the apache-commons-lang 3 library is not installed on your system. \n "
							+ "Download the jar file under https://commons.apache.org/proper/commons-lang/ and copy it to plugins/jars");
			return DONE;
		}

		// Abort macro if no image is open
		if (imp == null) {
			IJ.error("No image open");
			return DONE;
		}

		/*
		 * Because the optional parameters are easier to determine when the image has an
		 * non-inverted LUT, images with inverted LUT are reset.
		 */
		if (imp.isInvertedLut()) {
			IJ.showMessage("LUT reset",
					"The LUT of your image is inverted and will now reset for better parameter selection");
			IJ.run(imp, "Invert LUT", "");
		}

		Line.resetCounter();
		this.imp = imp;
		result = new ArrayList<Lines>();
		resultJunction = new ArrayList<Junctions>();
		readSettings();
		return DOES_8G + DOES_STACKS + FINAL_PROCESSING + PARALLELIZE_STACKS;
	}

	/**
	 * Sort lists.
	 */
	private void sortLists() {

		Collections.sort(result, new Comparator<Lines>() {

			@Override
			public int compare(Lines o1, Lines o2) {
				// TODO Auto-generated method stub
				if (o1.getFrame() < o2.getFrame())
					return -1;
				if (o1.getFrame() > o2.getFrame())
					return 1;
				return 0;
			}
		});
		Collections.sort(resultJunction, new Comparator<Junctions>() {

			@Override
			public int compare(Junctions o1, Junctions o2) {
				if (o1.getFrame() < o2.getFrame())
					return -1;
				if (o1.getFrame() > o2.getFrame())
					return 1;
				return 0;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ij.plugin.filter.ExtendedPlugInFilter#showDialog(ij.ImagePlus,
	 * java.lang.String, ij.plugin.filter.PlugInFilterRunner)
	 */
	@Override
	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr) {

		GenericDialogPlus gd = new GenericDialogPlus("Ridge Detection");
		gd.addMessage("Optional_parameters:");
		gd.addNumericField("Line_width", lineWidth, 1);
		gd.addNumericField("High_Contrast", contrastHigh, 0);
		gd.addNumericField("Low_Contrast", contrastLow, 0);
		gd.addMessage("Mandatory_parameters:");
		gd.addNumericField("Sigma", sigma, 2);
		gd.addNumericField("Lower_Threshold", lowerThresh, 2);
		gd.addNumericField("Upper_Threshold", upperThresh, 2);
		gd.addNumericField("Minimum_Line_Length", minLength, 2);
		gd.addNumericField("Maximum Line Length", maxLength, 2);
		gd.addCheckbox("Darkline", isDarkLine);
		gd.addCheckbox("Correct_position", doCorrectPosition);
		gd.addCheckbox("Estimate_width", doEstimateWidth);
		gd.addCheckbox("Extend_line", doExtendLine);
		gd.addCheckbox("Show_junction_points", showJunctionPoints);
		gd.addCheckbox("Show_IDs", showIDs);
		gd.addCheckbox("Verbose mode", verbose);
		gd.addCheckbox("DisplayResults", displayResults);
		gd.addCheckbox("Add_to_Manager", addToRoiManager);
		gd.addCheckbox("Make_Binary", makeBinary);
		gd.addCheckbox("Process_stack", doStack);

		final String[] overlap = new String[OverlapOption.values().length];
		for (int i = 0; i < overlap.length; i++) {
			overlap[i] = OverlapOption.values()[i].name();
		}

		gd.addChoice("Method_for_overlap_resolution", overlap, overlapOption.name());

		gd.addHelp("http://fiji.sc/Ridge_Detection");
		gd.addDialogListener(this);
		gd.addPreviewCheckbox(pfr, "Preview");
		gd.addButton("Reset settings to default", new ResetToDefaultListener(gd));
		gd.showDialog();
		if (gd.wasCanceled()) {
			imp.setOverlay(null);
			return DONE;
		} else {
			isPreview = false;
		}
		lineWidth = gd.getNextNumber();
		contrastHigh = gd.getNextNumber();
		contrastLow = gd.getNextNumber();
		sigma = gd.getNextNumber();
		lowerThresh = gd.getNextNumber();
		upperThresh = gd.getNextNumber();
		minLength = gd.getNextNumber();
		maxLength = gd.getNextNumber();
		isDarkLine = gd.getNextBoolean();
		doCorrectPosition = gd.getNextBoolean();
		doEstimateWidth = gd.getNextBoolean();
		doExtendLine = gd.getNextBoolean();
		showJunctionPoints = gd.getNextBoolean();
		showIDs = gd.getNextBoolean();
		verbose = gd.getNextBoolean();
		displayResults = gd.getNextBoolean();
		addToRoiManager = gd.getNextBoolean();
		makeBinary = gd.getNextBoolean();
		doStack = gd.getNextBoolean();
		overlapOption = OverlapOption.valueOf(gd.getNextChoice());
		saveSettings();

		result = new ArrayList<Lines>();
		resultJunction = new ArrayList<Junctions>();

		int labels = IJ.setupDialog(imp, DOES_8G + FINAL_PROCESSING + PARALLELIZE_STACKS);
		doStack = (labels != DOES_8G + FINAL_PROCESSING + PARALLELIZE_STACKS);

		return labels;
	}

	/**
	 * Read settings.
	 */
	private void readSettings() {
		lineWidth = Prefs.get("RidgeDetection.lineWidth", lineWidthDefault);
		contrastHigh = Prefs.get("RidgeDetection.contrastHigh", contrastHighDefault);
		contrastLow = Prefs.get("RidgeDetection.contrastLow", contrastLowDefault);
		sigma = Prefs.get("RidgeDetection.sigma", sigmaDefault);
		lowerThresh = Prefs.get("RidgeDetection.lowerThresh", lowerThreshDefault);
		upperThresh = Prefs.get("RidgeDetection.upperThresh", upperThreshDefault);
		minLength = Prefs.get("RidgeDetection.minLength", minLengthDefault);
		maxLength = Prefs.get("RidgeDetection.maxLength", maxLengthDefault);
		isDarkLine = Prefs.get("RidgeDetection.isDarkLine", isDarkLineDefault);
		doCorrectPosition = Prefs.get("RidgeDetection.doCorrectPosition", doCorrectPositionDefault);
		doEstimateWidth = Prefs.get("RidgeDetection.doEstimateWidth", doEstimateWidthDefault);
		doExtendLine = Prefs.get("RidgeDetection.doExtendLine", doExtendLineDefault);
		showJunctionPoints = Prefs.get("RidgeDetection.showJunctionPoints", showJunctionPointsDefault);
		showIDs = Prefs.get("RidgeDetection.showIDs", showIDsDefault);
		verbose = Prefs.get("RidgeDetection.verbose", verboseDefault);
		displayResults = Prefs.get("RidgeDetection.displayResults", displayResultsDefault);
		addToRoiManager = Prefs.get("RidgeDetection.addToRoiManager", addToRoiManagerDefault);
		makeBinary = Prefs.get("RidgeDetection.makeBinary", makeBinaryDefault);
		doStack = Prefs.get("RidgeDetection.doStack", doStackDefault);
		String overlapOptionString = Prefs.get("RidgeDetection.overlapOption", OverlapOption.NONE.name());
		overlapOption = OverlapOption.valueOf(overlapOptionString);

	}

	/**
	 * Save settings.
	 */
	private void saveSettings() {
		Prefs.set("RidgeDetection.lineWidth", lineWidth);
		Prefs.set("RidgeDetection.contrastHigh", contrastHigh);
		Prefs.set("RidgeDetection.contrastLow", contrastLow);
		Prefs.set("RidgeDetection.sigma", sigma);
		Prefs.set("RidgeDetection.lowerThresh", lowerThresh);
		Prefs.set("RidgeDetection.upperThresh", upperThresh);
		Prefs.set("RidgeDetection.minLength", minLength);
		Prefs.set("RidgeDetection.maxLength", maxLength);
		Prefs.set("RidgeDetection.isDarkLine", isDarkLine);
		Prefs.set("RidgeDetection.doCorrectPosition", doCorrectPosition);
		Prefs.set("RidgeDetection.doEstimateWidth", doEstimateWidth);
		Prefs.set("RidgeDetection.doExtendLine", doExtendLine);
		Prefs.set("RidgeDetection.showJunctionPoints", showJunctionPoints);
		Prefs.set("RidgeDetection.showIDs", showIDs);
		Prefs.set("RidgeDetection.verbose", verbose);
		Prefs.set("RidgeDetection.displayResults", displayResults);
		Prefs.set("RidgeDetection.addToRoiManager", addToRoiManager);
		Prefs.set("RidgeDetection.makeBinary", makeBinary);
		Prefs.set("RidgeDetection.doStack", doStack);
		Prefs.set("RidgeDetection.overlapOption", overlapOption.name());
	}

	/**
	 * Adds the to roi manager.
	 */
	public void addToRoiManager() {
		RoiManager rm = RoiManager.getInstance();
		if (rm == null) {
			rm = new RoiManager();

		}
		for (Lines contours : result) {
			for (Line c : contours) {

				float[] x = c.getXCoordinates();
				for (int j = 0; j < x.length; j++) {
					x[j] = (float) (x[j] + 0.5);
				}
				float[] y = c.getYCoordinates();
				for (int j = 0; j < y.length; j++) {
					y[j] = (float) (y[j] + 0.5);
				}

				FloatPolygon p = new FloatPolygon(x, y, c.getNumber());
				Roi r = new PolygonRoi(p, Roi.FREELINE);
				r.setPosition(c.getFrame());
				r.setName("C" + c.getID());

				rm.addRoi(r);

			}
		}
		for (Junctions junctions : resultJunction) {
			for (Junction j : junctions) {

				PointRoi pr = new PointRoi(j.x + 0.5, j.y + 0.5);
				pr.setName("JP-C" + j.getLine1().getID() + "-C" + j.getLine2().getID());
				pr.setPosition(j.getLine1().getFrame());
				rm.addRoi(pr);
			}
		}

		rm.setVisible(true);
		rm.runCommand("UseNames", "true");

	}

	/**
	 * Creates the results table.
	 *
	 * @param showJunctions
	 *            the show junctions
	 */
	private void createResultsTable(boolean showJunctions) {
		ResultsTable rt = ResultsTable.getResultsTable();
		ResultsTable rtSum = new ResultsTable();
		rt.setPrecision(3);

		Calibration cal = imp.getCalibration();
		for (Lines contours : result) {
			for (Line c : contours) {
				double meanWidth = 0;
				for (int i = 0; i < c.num; i++) {
					rt.incrementCounter();
					rt.addValue("Frame", contours.getFrame());

					rt.addValue("Contour ID", c.getID());
					rt.addValue("Pos.", i + 1);
					rt.addValue("X", c.col[i] * cal.pixelWidth);

					rt.addValue("Y", c.row[i] * cal.pixelHeight);
					rt.addValue("Length", c.estimateLength() * cal.pixelHeight);
					if (doCorrectPosition && doEstimateWidth) {
						rt.addValue("Contrast", Math.abs(c.intensity[i]));
						rt.addValue("Asymmetry", Math.abs(c.asymmetry[i]));
					}
					if (doEstimateWidth) {

						rt.addValue("Line width", (c.width_l[i] + c.width_r[i]) * cal.pixelWidth);
						meanWidth += c.width_l[i] + c.width_r[i];
						rt.addValue("Angle of normal", c.angle[i]);
					}
					rt.addValue("Class", c.getContourClass().toString().substring(5));
				}
				rtSum.incrementCounter();
				rtSum.addValue("Frame", contours.getFrame());
				rtSum.addValue("Contour ID", c.getID());
				rtSum.addValue("Length", c.estimateLength() * cal.pixelWidth);

				if (doEstimateWidth) {
					rtSum.addValue("Mean line width", meanWidth / c.num * cal.pixelWidth);
				}
			}
		}

		rt.show("Results");
		rtSum.show("Summary");

		if (showJunctions) {
			ResultsTable rt2 = new ResultsTable();
			rt2.setPrecision(0);
			for (Junctions junctions : resultJunction) {
				for (Junction j : junctions) {
					rt2.incrementCounter();
					rt2.addValue("Frame", junctions.getFrame());
					rt2.addValue("Contour ID 1", j.getLine1().getID());// c.get( j.cont1)
					rt2.addValue("Contour ID 2", j.getLine2().getID());
					rt2.addValue("X", j.x * cal.pixelWidth);
					rt2.addValue("Y", j.y * cal.pixelHeight);
				}
			}
			rt2.show("Junctions");
		}
	}

	/**
	 * Make binary.
	 */
	public void makeBinary() {
		ImagePlus binary = IJ.createHyperStack(imp.getTitle() + " Detected segments", imp.getWidth(), imp.getHeight(),
				imp.getNChannels(), imp.getStackSize() / imp.getNChannels(), 1, 8);
		binary.copyScale(imp);

		ImageProcessor binaryProcessor = binary.getProcessor();
		binaryProcessor.invertLut();
		if (imp.getCompositeMode() > 0) {
			((CompositeImage) binary).setLuts(imp.getLuts());
		}

		ImageStack is = binary.getImageStack();
		ImageProcessor ip = binary.getProcessor();

		for (Lines contours : result) {
			for (Line c : contours) {

				float[] x = c.getXCoordinates();
				float[] y = c.getYCoordinates();

				int[] x_poly_r = new int[x.length];
				int[] y_poly_r = new int[x.length];

				Polygon LineSurface = new Polygon();

				ip = is.getProcessor(c.getFrame());

				ip.setLineWidth(1);
				ip.setColor(255);

				for (int j = 0; j < x.length; j++) {
					// this draws the identified line
					if (j > 0) {
						ip.drawLine((int) Math.round(x[j - 1]), (int) Math.round(y[j - 1]), (int) Math.round(x[j]),
								(int) Math.round(y[j]));
					}

					// If Estimate Width is ticked, we also draw the line surface in the binary
					if (doEstimateWidth) {

						double nx = Math.sin(c.angle[j]);
						double ny = Math.cos(c.angle[j]);

						// left point coordinates are directly added to the polygon. right coordinates
						// are saved to be added at the end of the coordinates list
						LineSurface.addPoint((int) Math.round(x[j] - c.width_l[j] * nx),
								(int) Math.round(y[j] - c.width_l[j] * ny));

						x_poly_r[j] = (int) Math.round(x[j] + c.width_r[j] * nx);
						y_poly_r[j] = (int) Math.round(y[j] + c.width_r[j] * ny);
					}
				}

				if (doEstimateWidth) {
					// loop to add the right coordinates to the end of the polygon, reversed
					for (int j = 0; j < x.length; j++) {
						if (j < x.length) {
							LineSurface.addPoint(x_poly_r[x.length - 1 - j], y_poly_r[x.length - 1 - j]);
						}
					}
					// draw surfaces.
					ip.fillPolygon(LineSurface);
				}
			}
		}
		binary.show();
		binary.updateAndDraw();
	}

	/**
	 * Display contours.
	 */
	private void displayContours() {
		imp.setOverlay(null);
		Overlay ovpoly = new Overlay();

		double px, py, nx, ny, px_r = 0, py_r = 0, px_l = 0, py_l = 0;
		double last_w_r, last_w_l;

		// Print contour and boundary
		for (int k = 0; k < result.size(); k++) {
			for (int i = 0; i < result.get(k).size(); i++) {
				FloatPolygon polyMitte = new FloatPolygon();

				FloatPolygon polyR = new FloatPolygon();
				FloatPolygon polyL = new FloatPolygon();
				Line cont = result.get(k).get(i);
				int num_points = cont.num;
				last_w_r = 0;
				last_w_l = 0;

				for (int j = 0; j < num_points; j++) {

					px = cont.col[j];
					py = cont.row[j];
					nx = Math.sin(cont.angle[j]);
					ny = Math.cos(cont.angle[j]);
					if (doEstimateWidth) {
						px_r = px + cont.width_r[j] * nx;
						py_r = py + cont.width_r[j] * ny;
						px_l = px - cont.width_l[j] * nx;
						py_l = py - cont.width_l[j] * ny;
					}

					polyMitte.addPoint((px + 0.5), (py + 0.5));
					if (doEstimateWidth) {
						if (last_w_r > 0 && cont.width_r[j] > 0) {
							polyR.addPoint((px_r + 0.5), (py_r + 0.5));
						}
						if (last_w_l > 0 && cont.width_l[j] > 0) {
							polyL.addPoint((px_l + 0.5), (py_l + 0.5));
						}
					}
					if (doEstimateWidth) {
						last_w_r = cont.width_r[j];
						last_w_l = cont.width_l[j];
					}
				}

				PolygonRoi polyRoiMitte = new PolygonRoi(polyMitte, Roi.POLYLINE);

				polyRoiMitte.setStrokeColor(Color.red);
				int position = result.get(k).getFrame();
				if (!doStack || isPreview) {
					position = imp.getCurrentSlice();
				}

				polyRoiMitte.setPosition(position);
				ovpoly.add(polyRoiMitte);

				if (doEstimateWidth) {
					if (polyL.npoints > 1) {
						PolygonRoi polyRoiRand1 = new PolygonRoi(polyL, Roi.POLYLINE);
						polyRoiRand1.setStrokeColor(Color.green);
						position = result.get(k).getFrame();
						if (!doStack || isPreview) {
							position = imp.getCurrentSlice();
						}
						polyRoiRand1.setPosition(position);
						ovpoly.add(polyRoiRand1);

						PolygonRoi polyRoiRand2 = new PolygonRoi(polyR, Roi.POLYLINE);
						polyRoiRand2.setStrokeColor(Color.green);
						polyRoiRand2.setPosition(position);
						ovpoly.add(polyRoiRand2);
					}
				}

				// Show IDs
				if (showIDs) {/*
								 * int posx = polyMitte.xpoints[0]; int posy = polyMitte.ypoints[0];
								 * if(cont.cont_class == contour_class.cont_start_junc){ posx =
								 * polyMitte.xpoints[polyMitte.npoints-1]; posy =
								 * polyMitte.ypoints[polyMitte.npoints-1]; }
								 */

					int posx = (int) polyMitte.xpoints[polyMitte.npoints / 2];
					int posy = (int) polyMitte.ypoints[polyMitte.npoints / 2];
					TextRoi tr = new TextRoi(posx, posy, "" + cont.getID());
					tr.setCurrentFont(new Font(Font.SANS_SERIF, Font.PLAIN, 9));
					tr.setIgnoreClipRect(true);
					tr.setStrokeColor(Color.orange);
					tr.setPosition(resultJunction.get(k).getFrame());
					ovpoly.add(tr);
				}
			}
		}
		if (showJunctionPoints) {
			// Print junctions

			for (int k = 0; k < resultJunction.size(); k++) {
				FloatPolygon pointpoly = new FloatPolygon();
				for (int i = 0; i < resultJunction.get(k).size(); i++) {

					pointpoly.addPoint(resultJunction.get(k).get(i).x + 0.5, resultJunction.get(k).get(i).y + 0.5);
				}

				PointRoi pointroi = new PointRoi(pointpoly);
				pointroi.setShowLabels(false);
				int position = resultJunction.get(k).getFrame();
				if (!doStack || isPreview) {
					position = imp.getCurrentSlice();
				}
				pointroi.setPosition(position);
				ovpoly.add(pointroi);
			}
		}
		if (ovpoly.size() > 0) {
			imp.setOverlay(ovpoly);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ij.plugin.filter.ExtendedPlugInFilter#setNPasses(int)
	 */
	@Override
	public void setNPasses(int nPasses) {
		IJ.showProgress(nPasses, imp.getNSlices());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ij.gui.DialogListener#dialogItemChanged(ij.gui.GenericDialog,
	 * java.awt.AWTEvent)
	 */
	@Override
	public boolean dialogItemChanged(GenericDialog gd, AWTEvent e) {
		imp.setOverlay(null);
		boolean lwChanged = false;
		boolean contHighChanged = false;
		boolean contLowChanged = false;
		boolean darklineChanged = false;

		double lwCand = gd.getNextNumber();
		double diff = Math.abs(lwCand - lineWidth);
		if (diff > 0.0001) {
			lineWidth = lwCand;
			lwChanged = true;
		}

		double conCand = gd.getNextNumber();
		diff = Math.abs(conCand - contrastHigh);
		if (diff > 0.0001) {
			contrastHigh = conCand;
			contHighChanged = true;
		}

		conCand = gd.getNextNumber();
		diff = Math.abs(conCand - contrastLow);
		if (diff > 0.0001) {
			contrastLow = conCand;
			contLowChanged = true;
		}

		boolean darklineCand = gd.getNextBoolean();
		if (darklineCand != isDarkLine) {
			isDarkLine = darklineCand;
			darklineChanged = true;
		}

		doCorrectPosition = gd.getNextBoolean();
		doEstimateWidth = gd.getNextBoolean();
		doExtendLine = gd.getNextBoolean();
		showJunctionPoints = gd.getNextBoolean();
		showIDs = gd.getNextBoolean();
		verbose = gd.getNextBoolean();
		displayResults = gd.getNextBoolean();
		addToRoiManager = gd.getNextBoolean();
		makeBinary = gd.getNextBoolean();
		overlapOption = OverlapOption.valueOf(gd.getNextChoice());
		if (lwChanged || contHighChanged || contLowChanged) {
			contrastOrLineWidthChangedOnce = true;
		}

		if (lwChanged || contHighChanged || contLowChanged || (darklineChanged && contrastOrLineWidthChangedOnce)) {
			double estimatedSigma = lineWidth / (2 * Math.sqrt(3)) + 0.5;
			TextField textSigma = (TextField) gd.getNumericFields().get(3);
			textSigma.setText("" + IJ.d2s(estimatedSigma, 2));
			textSigma.setEditable(true);
			double clow = contrastLow;
			if (isDarkLine) {
				clow = 255 - contrastHigh;
			}
			double estimatedLowerThresh = Math.floor(Math.abs(-2 * clow * (lineWidth / 2.0)
					/ (Math.sqrt(2 * Math.PI) * estimatedSigma * estimatedSigma * estimatedSigma)
					* Math.exp(-((lineWidth / 2.0) * (lineWidth / 2.0)) / (2 * estimatedSigma * estimatedSigma))));
			TextField textLowThresh = (TextField) gd.getNumericFields().get(4);
			textLowThresh.setText("" + IJ.d2s(estimatedLowerThresh * 0.17, 2));
			textLowThresh.setEditable(true);
			double chigh = contrastHigh;
			if (isDarkLine) {
				chigh = 255 - contrastLow;
			}
			double estimatedUpperThresh = Math.floor(Math.abs(-2 * chigh * (lineWidth / 2.0)
					/ (Math.sqrt(2 * Math.PI) * estimatedSigma * estimatedSigma * estimatedSigma)
					* Math.exp(-((lineWidth / 2.0) * (lineWidth / 2.0)) / (2 * estimatedSigma * estimatedSigma))));
			TextField textUppThresh = (TextField) gd.getNumericFields().get(5);
			textUppThresh.setText("" + IJ.d2s(estimatedUpperThresh * 0.17, 2));
			textUppThresh.setEditable(true);
		}
		sigma = gd.getNextNumber();
		lowerThresh = gd.getNextNumber();
		upperThresh = gd.getNextNumber();
		if (lowerThresh >= upperThresh || sigma < 0.4 || Double.isNaN(sigma + lowerThresh + upperThresh)) {
			return false;
		}

		minLength = gd.getNextNumber();
		maxLength = gd.getNextNumber();

		isPreview = gd.isPreviewActive();

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ij.plugin.filter.PlugInFilter#run(ij.process.ImageProcessor)
	 */
	@Override
	public void run(ImageProcessor ip) {

		if (isPreview) {
			Line.resetCounter();
			result = new ArrayList<Lines>();
			resultJunction = new ArrayList<Junctions>();

		}

		LineDetector detect = new LineDetector();
		detect.bechatty = verbose;

		result.add(detect.detectLines(ip, sigma, upperThresh, lowerThresh, minLength, maxLength, isDarkLine,
				doCorrectPosition, doEstimateWidth, doExtendLine, overlapOption));
		usedOptions = detect.getUsedParamters();
		resultJunction.add(detect.getJunctions());

		if (isPreview) {
			displayContours();
			Line.resetCounter();
			result = new ArrayList<Lines>();
			resultJunction = new ArrayList<Junctions>();
		}
	}

	/**
	 * Return the detected lines.
	 *
	 * @return ArrayList of lines for each frame.
	 */
	public ArrayList<Lines> getDetectedLines() {
		return result;
	}

	/**
	 * Return the detected junctions.
	 *
	 * @return ArrayList of junctions for each frame.
	 */
	public ArrayList<Junctions> getDetectedJunctions() {
		return resultJunction;
	}

	/**
	 * Returns the parameter which were used for the analysis.
	 *
	 * @return The used parameters
	 */
	public Options getParameters() {
		return usedOptions;
	}

}
