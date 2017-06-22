package com.sortable.similarity;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sortable.similarity.impl.Damerau;
import com.sortable.similarity.impl.SorensenDice;
import com.sortable.similarity.model.Listing;
import com.sortable.similarity.model.Product;
import com.sortable.similarity.model.Result;
import com.sortable.similarity.tree.BkTree;
import com.sortable.similarity.tree.BkTreeSearcher;
import com.sortable.similarity.tree.MutableBkTree;

public class ProductListing {
	
	public static  class Tuple<T1, T2>{
			
			T1 field1;
			T2 field2;
			
			public Tuple(T1 f1, T2 f2){
				this.field1 = f1;
				this.field2 =f2;
			}
			
			public T1 field1(){
				return field1;
			}
			public T2 field2(){
				return field2;
			}
			
			public String toString(){
				return field1+"::"+field2;
			}
			
		}


	public static String getArg(int pos, int argLength,String[] args){
		if( argLength >pos){
			if(args[pos]!=null&& args[pos].trim().length()>0)
				return args[pos].trim();
		}
		return null;
	}
	
	public static final int MAX_BK_SEARCH=100;
	public static final double DICE_CUTOFF=0.5d;

	
	/*
	 * 
	 * The code first uses the Soresendice as the way to find the closest manufacturer for the listing.
	 *  Once that is got it uses a bktree for the closest product match..
	 *  
	 *  https://stats.stackexchange.com/questions/195006/is-the-dice-coefficient-the-same-as-accuracy 
	 *  http://blog.notdot.net/2007/4/Damn-Cool-Algorithms-Part-1-BK-Trees
	 * 
	 */
	
	public static void main(String[] args) throws IOException {
		String productsListing ="./products.txt";
		String listingFile = "./listings.txt";
		int argLength = args.length;
		
		String fileName = getArg(0,argLength,args);
		if(fileName!= null){
			productsListing = fileName;
			fileName=null;
		}
		 fileName = getArg(1,argLength,args);
		 if(fileName!= null){
			 listingFile = fileName;
		 }
		long start = System.currentTimeMillis();
		ObjectMapper mapper = new ObjectMapper();

	
		
		Map<String,List<String>> allProducts = Files.readAllLines(Paths.get(new File(productsListing).toURI())).stream().map(s -> {
			try {
				return mapper.readValue(s, Product.class);
			} catch (Exception ex) {
				throw new RuntimeException(ex);
				
			}
		}).collect(Collectors.toMap(Product::getManufacturer,//key mapper
									(p->{return Arrays.asList(p.getProductName());}), //value mapper
									//merge on duplicates
									((l1,l2)->{ 
										List<String> merged = new ArrayList(l1); 
										merged.addAll(l2); 
										return merged;
										})
									));
		Map<String,BkTree> manufactererBkTree = new HashMap<String,BkTree>();
		Set<String> allManufacterers = new HashSet<String>();
		for(Entry<String,List<String>> e: allProducts.entrySet()){
			
			String manufacturer =e.getKey();
			allManufacterers.add(manufacturer);
			MutableBkTree tree = new MutableBkTree(new Damerau());
			for(String product:e.getValue()){
				tree.add(product);
			}
			manufactererBkTree.put(manufacturer, tree);
		}
		SorensenDice dice = new SorensenDice();
		FileWriter errFw = new FileWriter(new File("./error-manufacturer-notmatched.txt"));
		Iterator<Optional<Tuple<String,Listing>>> matched=	Files.readAllLines(Paths.get(new File(listingFile).toURI())).stream().map(s -> {
			try {
				return mapper.readValue(s, Listing.class);
			} catch (Exception ex) {
				ex.printStackTrace();
				return null;
			}
		}).filter(l->{  String lstManufacterer = l.getManufacturer();
						if( manufactererBkTree.get(lstManufacterer)!= null)
							return true;
						else{
							double maxSimilarity = 0.0;
							String manufacterWithMaxScore = null;
							
							for(String manufacterer:allManufacterers){
								double currDistance =dice.similarity(manufacterer, lstManufacterer);
								if(maxSimilarity<currDistance){
									maxSimilarity = currDistance;
									manufacterWithMaxScore=manufacterer;
								}
							}
							if(manufacterWithMaxScore != null && maxSimilarity >= DICE_CUTOFF){
								
								BkTree tree = manufactererBkTree.get(manufacterWithMaxScore);
								manufactererBkTree.put(l.getManufacturer(),tree);
								return true;
							}else{
								
								try {
									errFw.write(l.getManufacturer() +"::"+l.getTitle()+"\n");
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								return false;
							}
						}
							
					})
		   .map(l -> {
					BkTree tree = manufactererBkTree.get(l.getManufacturer());
					
					BkTreeSearcher searcher = new BkTreeSearcher(tree);
					return searcher.search(l.getTitle(), MAX_BK_SEARCH).stream()
					.reduce((m1, m2) -> m1.getDistance() < m2.getDistance() ? m1 : m2)
					.map((m) -> {
								return new Tuple<String, Listing>(m.getMatch(), l);
						});
					}).iterator();
		Map<String,List<Listing>> finalResult = new HashMap<String,List<Listing>>();
		while(matched.hasNext()){
			matched.next().ifPresent( t->finalResult.merge(t.field1(), Arrays.asList(t.field2()),
										(l1,l2)->{
											List<Listing> l3=new ArrayList<Listing>(l1);
											l3.addAll(l2); 
											return l3;
											} 
										));
		}

		
		FileWriter fw = new FileWriter(new File("./results.txt"));
		
		for (Entry<String, List<Listing>> entry : finalResult.entrySet()) {
		
			Result r = new Result();
			r.setProductName(entry.getKey());

			r.setListings(entry.getValue());
		
			fw.write(mapper.writeValueAsString(r) + "\n");

		}

		fw.flush();
		System.out.println("Done in "+(System.currentTimeMillis()-start)/1000);
	}

}
