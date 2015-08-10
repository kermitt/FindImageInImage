package find;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

/**
 * Recieves two images. A 'context' images and a 'target' image This looks to
 * see if the target is inside the context. It returns true or false
 * 
 * TODO: A whole lot of things...   ...like unit tests and add try catch...
 * This was just a curiousity so this is pretty sloppy stuff
 */

public class FindImageInImageMain {

	/*
	 * Recieves two file names: the context better not be a smaller image than
	 * the target
	 */
	public boolean isInside(String filename_context, String filename_target) {

		BufferedImage context = getBufferedImageFromFileName(filename_context);
		BufferedImage target = getBufferedImageFromFileName(filename_target);

		Pixel unique = findUniquest_step1(target); // find the most unique pixel
													// value in this
													// bufferedimage
		List<Pixel> candidates = findCandidateLocations_step2(context,
				unique.value);
		candidates = examine(target, unique, context, candidates);
		
		boolean yes_target_is_inside_context = contextHasTarget(target, unique,
				context, candidates);

		return yes_target_is_inside_context;

	}

	private List<Pixel> findCandidateLocations_step2(BufferedImage context,
			int targetValue) {
		int width = context.getWidth();
		int height = context.getHeight();

		List<Pixel> candidates = new ArrayList<Pixel>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (context.getRGB(x, y) == targetValue) {
					Integer value = context.getRGB(x, y);
					Pixel pixel = new Pixel(value, x, y);
					candidates.add(pixel);
				}
			}
		}
		return candidates;
	}

	private List<Pixel> examine(BufferedImage target, Pixel pixel,
			BufferedImage context, List<Pixel> candidates) {
		List<Pixel> potential_matching_subimages_from_context = new ArrayList<Pixel>();
		int t_height = target.getHeight();
		int[] findme = new int[t_height];
		int i = 0;
		for (int y = 0; y < t_height; y++) {
			int rgb = target.getRGB(pixel.x, y);
			findme[i] = rgb;
			i++;
		}

		for (Pixel candidate : candidates) {
			int cx = candidate.x;
			int cy = candidate.y;
			int floor = cy - pixel.y;
			int ceiling = floor + target.getHeight();
			int[] findme2 = new int[t_height];
			int j = 0;
			for (int y = cy - pixel.y; y < ceiling; y++) {
				findme2[j] = context.getRGB(cx, y);
				j++;
			}
			boolean isSame = areTheseTwo1DArraysTheSame(findme2, findme);
			if (isSame) {
				potential_matching_subimages_from_context.add(candidate);
			}
		}
		return potential_matching_subimages_from_context;
	}

	private boolean contextHasTarget(BufferedImage target, Pixel pixel,
			BufferedImage context, List<Pixel> candidates) {
		if (candidates.size() > 0) {
			int t_width = target.getWidth();
			int t_height = target.getHeight();
			boolean contextContainsTarget = true;

			for (Pixel candidate : candidates) {
				int floorY = candidate.y - pixel.y;
				int floorX = candidate.x - pixel.x;

				for (int x = 0; x < t_width; x++) {
					for (int y = 0; y < t_height; y++) {

						int t_rgb = target.getRGB(x, y);
						int c_rgb = context.getRGB(x + floorX, y + floorY);

						if (t_rgb != c_rgb) {
							contextContainsTarget = false;
						}
					}
				}
			}
			return contextContainsTarget;
		} else {
			return false;
		}
	}

	private Pixel findUniquest_step1(BufferedImage target) {
		int width = target.getWidth();
		int height = target.getHeight();
		Map<Integer, Pixel> pixels = new HashMap<Integer, Pixel>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				Integer value = target.getRGB(x, y);
				Pixel pixel = new Pixel(value, x, y);
				if (pixels.containsKey(value)) {
					pixels.get(value).seen++;
				} else {
					pixels.put(value, pixel);
				}
			}
		}
		// pixel that is seen just once will do
		Pixel found = null;
		for (Integer I : pixels.keySet()) {
			if (pixels.get(I).seen == 1) {
				found = pixels.get(I);
				break;
			}
		}
		return found;
	}

	public boolean areTheseTwo1DArraysTheSame(int[] a, int[] b) {
		if (a.length != b.length && a.length > 0) {
			return false;
		}
		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}
		return true;
	}

	private static BufferedImage getBufferedImageFromFileName( String filename ) { 
		File img = new File( filename );
		try {
			BufferedImage bi = ImageIO.read(img);
			return bi;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; 
	}
	/* 
	private static List < Pixel>  getCandidates(Pixel target, BufferedImage context) {

		int height = context.getHeight();
		int width = context.getWidth();

		List < Pixel> candidates = new ArrayList < Pixel >(); 
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if ( context.getRGB(x, y) == target.value ) { 
					candidates.add( new Pixel( target.value, x, y )) ;
				}
			}
		}
		
		return candidates; 
	}
	*/
	
	public void log(String s) {
		System.out.println(s);
	}
}