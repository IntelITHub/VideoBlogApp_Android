package com.iih.android.videoblog.Parsing;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Handler;
import android.os.Message;

/**
 * @author hb
 * 
 */
@SuppressWarnings("unchecked")	

public class JSONParser implements Runnable {
	// private ProgressDialog pd;
	// private Activity activity;

	private String JSON_URL = null;

	private JSONParsingListener jsonParsingListener = null;

	private ArrayList<Object> jsonDataArrayList = new ArrayList<Object>();

	private LinkedHashMap<String, Object> jsonDataHashMapList = new LinkedHashMap<String, Object>();

	private ArrayList<String> keysList = new ArrayList<String>();

	private ArrayList<Object> searchedList = new ArrayList<Object>();

	private ArrayList<Object> searchedListInParentTag = new ArrayList<Object>();

	private String keyInParentTag = null;

	/**
	 * @param a
	 *            context of Activity in which you want to use this Utility
	 */
	public JSONParser() {
		// activity = a;
	}

	/**
	 * @param jsonParsingListener
	 */
	public void setJSONParserListener(JSONParsingListener jsonParsingListener) {
		if (jsonParsingListener != null) {
			this.jsonParsingListener = jsonParsingListener;
		}
	}

	/*
	 * private boolean isNetworkAvailable() { boolean connection = false; try {
	 * ConnectivityManager cm = (ConnectivityManager)
	 * activity.getSystemService(Context.CONNECTIVITY_SERVICE); if (cm != null)
	 * { NetworkInfo net_info = cm.getActiveNetworkInfo(); if (net_info != null)
	 * connection = true; } } catch (Exception e) { e.printStackTrace(); }
	 * return connection; }
	 */

	@Override
	public void run() {
		Message msg = new Message();
		msg.what = 1;

		if (isURLValid())// && isNetworkAvailable() )
		{
			try {
				URL url = new URL(JSON_URL);
				URLConnection urlc = url.openConnection();
				HttpURLConnection huc = (HttpURLConnection) urlc;
				huc.setRequestMethod("GET");
				huc.connect();
				int response = huc.getResponseCode();
				if (response == HttpURLConnection.HTTP_OK) {
					InputStream is = huc.getInputStream();
					final String result = convertStreamToString(is);
					parseData(result);
				} else {
					msg.what = 0;
				}
			} catch (Exception e) {
				msg.what = 0;
				e.printStackTrace();
			}
		} else {
			msg.what = 0;

		}
		handler.sendMessage(msg);
	}

	private void parseData(final String jsonString) {
		if (jsonString != null) {
			if (!jsonString.equals("")) {
				JSONTokener jt = null;
				JSONArray jsonArray = null;
				JSONObject jsonObject = null;
				try {
					jt = new JSONTokener(jsonString);
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					jsonArray = new JSONArray(jt);
					ArrayList<Object> arrayList = (ArrayList<Object>) parseJSONArray(jsonArray);
					if (arrayList.size() > 0) {
						jsonDataArrayList = new ArrayList<Object>(arrayList);
						arrayList.clear();
					}
				} catch (Exception e0) {
					e0.printStackTrace();
					try {
						jsonObject = new JSONObject(jt);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (jsonObject != null) {
						LinkedHashMap<String, Object> LinkedHashMap = (LinkedHashMap<String, Object>) parseJSONObject(jsonObject);
						if (LinkedHashMap.size() > 0) {
							jsonDataHashMapList = LinkedHashMap;
						}
					} else // Still u are getting Error Convert the result
							// directly to JSONArray
					{
						try {
							jsonArray = new JSONArray(jsonString);
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (jsonArray == null) // Still u are getting Error
												// Convert the result directly
												// to JSONObject
						{
							try {
								jsonObject = new JSONObject(jsonString);
							} catch (Exception e) {
								e.printStackTrace();
							}

							if (jsonObject != null) {
								LinkedHashMap<String, Object> LinkedHashMap = (LinkedHashMap<String, Object>) parseJSONObject(jsonObject);
								if (LinkedHashMap.size() > 0) {
									jsonDataHashMapList = LinkedHashMap;
								}
							}
						} else {
							ArrayList<Object> arrayList = (ArrayList<Object>) parseJSONArray(jsonArray);
							if (arrayList.size() > 0) {
								jsonDataArrayList = new ArrayList<Object>(arrayList);
								arrayList.clear();
							}
						}
					}
				}

//				if (jsonArray == null) // Bad Webservice may be it is starting
//										// with JSON OBject
//				{
//					try {
//						jsonObject = new JSONObject(jt);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//					if (jsonObject != null) {
//						LinkedHashMap<String, Object> LinkedHashMap = (LinkedHashMap<String, Object>) parseJSONObject(jsonObject);
//						if (LinkedHashMap.size() > 0) {
//							jsonDataHashMapList = LinkedHashMap;
//						}
//					} else // Still u are getting Error Convert the result
//							// directly to JSONArray
//					{
//						try {
//							jsonArray = new JSONArray(jsonString);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//
//						if (jsonArray == null) // Still u are getting Error
//												// Convert the result directly
//												// to JSONObject
//						{
//							try {
//								jsonObject = new JSONObject(jsonString);
//							} catch (Exception e) {
//								e.printStackTrace();
//							}
//
//							if (jsonObject != null) {
//								LinkedHashMap<String, Object> LinkedHashMap = (LinkedHashMap<String, Object>) parseJSONObject(jsonObject);
//								if (LinkedHashMap.size() > 0) {
//									jsonDataHashMapList = LinkedHashMap;
//								}
//							}
//						} else {
//							ArrayList<Object> arrayList = (ArrayList<Object>) parseJSONArray(jsonArray);
//							if (arrayList.size() > 0) {
//								jsonDataArrayList = new ArrayList<Object>(arrayList);
//								arrayList.clear();
//							}
//						}
//					}
//				} else {
//					ArrayList<Object> arrayList = (ArrayList<Object>) parseJSONArray(jsonArray);
//					if (arrayList.size() > 0) {
//						jsonDataArrayList = new ArrayList<Object>(arrayList);
//						arrayList.clear();
//					}
//				}
			}
		}
	   
		
		// Updated june 23 by Ashish
		
		if(jsonDataHashMapList.size()>0)
			jsonDataArrayList.add(jsonDataHashMapList);
	
	}

	private boolean isURLValid() {
		boolean success = false;

		if (JSON_URL != null) {
			JSON_URL = JSON_URL.trim();

			if (!JSON_URL.equalsIgnoreCase("")) {
				if (JSON_URL.startsWith("http")) {
					success = true;
				}
			}
		}
		return success;
	}

	private ArrayList<Object> parseJSONArray(JSONArray jsonArray) {
		ArrayList<Object> arrayList = new ArrayList<Object>();

		int length = jsonArray.length();
		for (int i = 0; i < length; i++) {
			try {
				Object o = jsonArray.get(i);
				if (o != null) {
					String className = o.getClass().getCanonicalName()
							.toString();
					if (className != null) {
						if (className.equals("org.json.JSONArray")) {
							JSONArray subArray = (JSONArray) o;
							Object object = parseJSONArray(subArray);
							if (object != null) {
								arrayList.add(object);
							}
						} else if (className.equals("org.json.JSONObject")) {
							JSONObject subObject = (JSONObject) o;
							Object object = parseJSONObject(subObject);
							if (object != null) {
								arrayList.add(object);
							}
						} else if (className.equals("java.lang.String")) {
							String s = (String) o;
							arrayList.add(s);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return arrayList;
	}

	private LinkedHashMap<String, Object> parseJSONObject(JSONObject jsonObject) {
		LinkedHashMap<String, Object> LinkedHashMap = new LinkedHashMap<String, Object>();

		for (Iterator<?> iterator = jsonObject.keys(); iterator.hasNext();) {
			String key = (String) iterator.next();
			Object value = null;
			if (key != null) {
				key = key.trim();
				if (!key.equals("")) {
					try {
						value = jsonObject.getString(key);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (value != null) {
						String className = null;
						try {
							className = value.getClass().getCanonicalName().toString();
						} catch (Exception e) {
							className = "";
						}

						if (className.equals("org.json.JSONObject")) {
							JSONObject jsonObject2 = (JSONObject) value;
							Object o = parseJSONObject(jsonObject2);
							LinkedHashMap.put(key, o);
						} else if (className.equals("org.json.JSONArray")) {
							JSONArray jsonArray = (JSONArray) value;
							Object o = parseJSONArray(jsonArray);
							LinkedHashMap.put(key, o);
						} else if (className.equalsIgnoreCase("java.lang.String")) {
							String stringValue = (String) value; // if there is
																	// JSONArray
																	// or
																	// JSONObject
																	// inside
																	// String

							stringValue = stringValue.trim();

							if (stringValue.startsWith("[")) {
								JSONArray jsonStirngArray = getJSONArrayFromString(stringValue);
								if (jsonStirngArray != null) {
									Object o = parseJSONArray(jsonStirngArray);
									LinkedHashMap.put(key, o);
								}
							} else if (stringValue.startsWith("{")) {
								JSONObject jsonStringObject = getJSONObjectFromString(stringValue);
								if (jsonStringObject != null) {
									Object o = parseJSONObject(jsonStringObject);
									LinkedHashMap.put(key, o);
								}
							} else {
								LinkedHashMap.put(key, value);
							}
						}
					} else {
						LinkedHashMap.put(key, value);
					}
				}
			}
		}
		return LinkedHashMap;
	}

	private JSONObject getJSONObjectFromString(String stringValue) {
		JSONObject jsonObject = null;
		try {
			JSONTokener jsonTokener = new JSONTokener(stringValue);
			jsonObject = new JSONObject(jsonTokener);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonObject == null) {
			try {
				jsonObject = new JSONObject(stringValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return jsonObject;

	}

	private JSONArray getJSONArrayFromString(String stringValue) {
		JSONArray jsonArray = null;
		try {
			JSONTokener jsonTokener = new JSONTokener(stringValue);
			jsonArray = new JSONArray(jsonTokener);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (jsonArray == null) {
			try {
				jsonArray = new JSONArray(stringValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return jsonArray;
	}

	private String convertStreamToString(InputStream inputStream) {
		boolean error = false;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			error = true;
		} finally {
			try {
				inputStream.close();
			} catch (Exception e) {
				error = true;
			}
		}
		if (error)
			return null;
		else
			return sb.toString();
	}

	
	// Updated june 23 by kk
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
            sendResponse();
		}
	};

	/**
	 * @param jsonString
	 *            string which contains the JSON data<br/>
	 *            parses the <b>jsonString</b> and calls the method
	 *            <b>onJSONParsingFinished</b> after parsing is completed
	 * 
	 */
	public void parse(String jsonString) // Change here also
	{
		if (this.jsonParsingListener != null) {

			parseData(jsonString);
			sendResponse();
		}
	}
	
	
	private void sendResponse()
	{
		// if(pd.isShowing())
					// pd.dismiss();

						// Searching whether he passed Keys or not
						if (keysList.size() > 0) 
						{
							 searchKeysFromJSONData(0);

							if (keyInParentTag != null) 
							{
								searchkeyInParentTags();

								searchedList.clear();

								searchedListInParentTag = deleteUnnecessaryNestedHashMaps(searchedListInParentTag);
								jsonParsingListener.onJSONParsingFinished(searchedListInParentTag, true);
							} 
							else 
							{
								searchedList = deleteUnnecessaryNestedHashMaps(searchedList);
								jsonParsingListener.onJSONParsingFinished(searchedList,true);
							}
						} 
						else 
						{
							jsonDataArrayList = deleteUnnecessaryNestedHashMaps(jsonDataArrayList);
							jsonParsingListener.onJSONParsingFinished(jsonDataArrayList, true);
						}
	}

	@SuppressWarnings("rawtypes")
	private ArrayList<Object> deleteUnnecessaryNestedHashMaps(ArrayList<Object> JsonArrayList) {
		int size = JsonArrayList.size();
		if (size == 1) {
			Object object = JsonArrayList.get(0);
			String className = object.getClass().getSimpleName();
			if (className.equals("ArrayList")) {
				ArrayList<Object> deletedArrayList = (ArrayList<Object>) object;
				size = deletedArrayList.size();
				if (size == 1)
					return deleteUnnecessaryNestedHashMaps(deletedArrayList);
				else
					return deletedArrayList;
			} else if (className.equals("LinkedHashMap")) {
				LinkedHashMap<String, Object> linkedHashMap = (LinkedHashMap<String, Object>) object;
				int hashSize = linkedHashMap.size();
				if (hashSize == 1) {
					Set<String> keySet = linkedHashMap.keySet();
					for (Iterator iterator = keySet.iterator(); iterator.hasNext();) {
						String key = (String) iterator.next();
						object = linkedHashMap.get(key);
					}

					ArrayList<Object> arrayList = new ArrayList<Object>();

					className = object.getClass().getSimpleName();
					if (className.equals("ArrayList")) {
						arrayList = (ArrayList<Object>) object;
					} else if (className.equals("LinkedHashMap")) {
						arrayList.add(object);
					} else {
						arrayList = JsonArrayList;
					}
					return arrayList;
				} else
					return JsonArrayList;
			} else {
				return JsonArrayList;
			}
		} else
			return JsonArrayList;
	}

	/**
	 * @param url
	 *            url which contains the JSON data<br/>
	 *            parses the <b>url</b> and calls the method
	 *            <b>onJSONParsingFinished</b> after parsing is completed
	 * 
	 */
	public void parse(URL url) {
		if (this.jsonParsingListener != null) {
			if (url != null)
				this.JSON_URL = url.toString();

			// pd = new ProgressDialog(activity);
			// pd.setMessage("Loading Data ...");
			// pd.show();
			Thread t = new Thread(this);
			t.start();
		}
	}

	/**
	 * @param url
	 *            url which contains the JSON data
	 * @param keys
	 *            array by which you can filter the json Data <br/>
	 *            parses the <b>url</b> and calls the method
	 *            <b>onJSONParsingFinished</b> after parsing is completed.This
	 *            method will return the data objects in which it contains the
	 *            <b>keys</b> which you have passed
	 * 
	 */
	public void parseByKeys(URL url, String[] keys) {
		if (this.jsonParsingListener != null) {

			if (keys != null) {
				if (keys.length > 0) {
					this.keysList = new ArrayList<String>(Arrays.asList(keys));
				}
			}
			parse(url);

		}
	}

	/**
	 * @param jsonString
	 *            string which contains the JSON data
	 * @param keys
	 *            array by which you can filter the json Data parses the
	 *            <b>jsonString</b> and calls the method
	 *            <b>onJSONParsingFinished</b> after parsing is completed.This
	 *            method will return the data objects in which it contains the
	 *            <b>keys</b> which you have passed
	 * 
	 */

	public void parseByKeys(String jsonString, String[] keys) {
		if (this.jsonParsingListener != null) {

			if (keys != null) {
				if (keys.length > 0) {
					this.keysList = new ArrayList<String>(Arrays.asList(keys));
				}
			}

			parse(jsonString);

		}
	}

	/**
	 * @param url
	 *            url which contains the JSON data<br/>
	 * @param key
	 *            key by which you can filter the json Data <br/>
	 * @param parentTag
	 *            tag name in whose data you want to parse <br/>
	 *            parses the <b>url</b> and calls the method
	 *            <b>onJSONParsingFinished</b> after parsing is completed.This
	 *            method will return the data objects which are children of the
	 *            <b>parentTag</b> and which contains the key that you have
	 *            passed
	 * 
	 */
	public void parseByKey(URL url, String key, String parentTag) {
		if (this.jsonParsingListener != null) {

			if (parentTag != null) {
				parentTag = parentTag.trim();

				if (parentTag.length() > 0) {
					this.keysList = new ArrayList<String>();
					this.keysList.add(parentTag);
				}
			}

			if (key != null) {
				key = key.trim();

				if (key.length() > 0) {
					keyInParentTag = key;
				}
			}

			parse(url);

		}
	}

	/**
	 * @param jsonString
	 *            jsonString which contains the JSON data<br/>
	 * @param key
	 *            key by which you can filter the json Data <br/>
	 * @param parentTag
	 *            tag name in whose data you want to parse <br/>
	 *            parses the <b>jsonString</b> and calls the method
	 *            <b>onJSONParsingFinished</b> after parsing is completed.This
	 *            method will return the data objects which are children of the
	 *            <b>parentTag</b> and which will contain the key that you have
	 *            passed
	 * 
	 */
	public void parseByKey(String jsonString, String key, String parentTag) {
		if (this.jsonParsingListener != null) {

			if (parentTag != null) {
				parentTag = parentTag.trim();

				if (parentTag.length() > 0) {
					this.keysList = new ArrayList<String>();
					this.keysList.add(parentTag);
				}
			}

			if (key != null) {
				key = key.trim();

				if (key.length() > 0) {
					keyInParentTag = key;
				}
			}

			parse(jsonString);

		}
	}

	private void searchKeysFromJSONData(int type) {
		if (type == 0) {
			getKeysFromArrayList(jsonDataArrayList);
		} else {
			getKeysFromLinkedHashMap(jsonDataHashMapList);
		}
	}

	private void getKeysFromLinkedHashMap(
			LinkedHashMap<String, Object> linkedHashMap) {
		if (linkedHashMap != null) {
			int size = linkedHashMap.size();

			if (size > 0) {
				int keys = keysList.size();

				LinkedHashMap<String, Object> LinkedHashMapdUMMY = new LinkedHashMap<String, Object>();
				for (int i = 0; i < keys; i++) {
					String KEY = null;
					try {
						KEY = keysList.get(i);
					} catch (Exception e) {
						e.printStackTrace();
					}

					if (KEY != null) {
						KEY = KEY.toString().trim();

						if (!KEY.equalsIgnoreCase("")) {
							if (linkedHashMap.containsKey(KEY)) {
								Object VALUE = linkedHashMap.get(KEY);
								LinkedHashMapdUMMY.put(KEY, VALUE);
							}
						}
					}
				}

				if (LinkedHashMapdUMMY.size() > 0) {
					searchedList.add(LinkedHashMapdUMMY);
				}

				Collection<Object> collection = linkedHashMap.values();
				for (Iterator<Object> iterator = collection.iterator(); iterator
						.hasNext();) {
					Object value = (Object) iterator.next();
					if (value != null) {
						String className = value.getClass().getSimpleName()
								.toString();
						if (className.equals("ArrayList")) {
							ArrayList<Object> arrayList = (ArrayList<Object>) value;
							getKeysFromArrayList(arrayList);
						} else if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> LinkedHashMap = (LinkedHashMap<String, Object>) value;
							getKeysFromLinkedHashMap(LinkedHashMap);
						}
					}
				}
			}
		}
	}

	private void getKeysFromArrayList(ArrayList<Object> arrayList) {
		if (arrayList != null) {
			int size = arrayList.size();

			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Object value = arrayList.get(i);
					if (value != null) {
						String className = value.getClass().getSimpleName()
								.toString();
						if (className.equals("ArrayList")) {
							ArrayList<Object> list = (ArrayList<Object>) value;
							getKeysFromArrayList(list);
						} else if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> LinkedHashMap = (LinkedHashMap<String, Object>) value;
							getKeysFromLinkedHashMap(LinkedHashMap);
						}
					}
				}
			}
		}
	}

	private void searchkeyInParentTags() {
		int size = searchedList.size();

		for (int i = 0; i < size; i++) {
			Object value = searchedList.get(i);
			if (value != null) {
				String className = value.getClass().getSimpleName().toString();
				if (className.equals("ArrayList")) {
					ArrayList<Object> list = (ArrayList<Object>) value;
					getParentKeysFromArrayList(list);
				} else if (className.equals("LinkedHashMap")) {
					LinkedHashMap<String, Object> LinkedHashMap = (LinkedHashMap<String, Object>) value;
					getParentKeysFromLinkedHashMap(LinkedHashMap);
				}
			}
		}

	}

	private void getParentKeysFromArrayList(ArrayList<Object> arrayList) {
		if (arrayList != null) {
			int size = arrayList.size();

			if (size > 0) {
				for (int i = 0; i < size; i++) {
					Object value = arrayList.get(i);
					if (value != null) {
						String className = value.getClass().getSimpleName()
								.toString();
						if (className.equals("ArrayList")) {
							ArrayList<Object> list = (ArrayList<Object>) value;
							getParentKeysFromArrayList(list);
						} else if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> LinkedHashMap = (LinkedHashMap<String, Object>) value;
							getParentKeysFromLinkedHashMap(LinkedHashMap);
						}
					}
				}
			}
		}
	}

	private void getParentKeysFromLinkedHashMap(
			LinkedHashMap<String, Object> linkedHashMap) {
		if (linkedHashMap != null) {
			int size = linkedHashMap.size();

			if (size > 0) {
				if (linkedHashMap.containsKey(keyInParentTag)) {
					Object VALUE = linkedHashMap.get(keyInParentTag);
					searchedListInParentTag.add(VALUE);
				}

				Collection<Object> collection = linkedHashMap.values();
				for (Iterator<Object> iterator = collection.iterator(); iterator
						.hasNext();) {
					Object value = (Object) iterator.next();
					if (value != null) {
						String className = value.getClass().getSimpleName()
								.toString();
						if (className.equals("ArrayList")) {
							ArrayList<Object> arrayList = (ArrayList<Object>) value;
							getParentKeysFromArrayList(arrayList);
						} else if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> LinkedHashMap = (LinkedHashMap<String, Object>) value;
							getParentKeysFromLinkedHashMap(LinkedHashMap);
						}
					}
				}
			}
		}
	}

	public LinkedHashMap<String, Object> removeAllObjectsFromLinkedHashMap(
			String value, LinkedHashMap<String, Object> srcLinkedHashMap) {
		if (srcLinkedHashMap != null) {
			if (srcLinkedHashMap.size() > 0) {
				Set<Entry<String, Object>> entrySet = (Set<Entry<String, Object>>) srcLinkedHashMap
						.entrySet(); // Entry Set
				for (Iterator<Entry<String, Object>> iterator = entrySet
						.iterator(); iterator.hasNext();) {
					Entry<String, Object> entry = iterator.next();

					Object object = entry.getValue();

					if (object == null) {
						if (value == null)
							iterator.remove();
					} else if (value != null) {
						String className = object.getClass().getSimpleName();
						if (className.equals("String")) {
							String stringObject = (String) object;
							if (stringObject.equalsIgnoreCase(value))
								iterator.remove();
						} else if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> nestedLinkedHashMap = (LinkedHashMap<String, Object>) object;
							entry.setValue(removeAllObjectsFromLinkedHashMap(
									value, nestedLinkedHashMap));
						} else if (className.equals("ArrayList")) {
							ArrayList<Object> nestedArrayList = (ArrayList<Object>) object;
							entry.setValue(removeAllObjectsFromArrayList(value,
									nestedArrayList));
						}
					}
				}
			}
		}

		return srcLinkedHashMap;
	}

	public ArrayList<Object> removeAllObjectsFromArrayList(String value,
			ArrayList<Object> arrayList) {
		if (arrayList != null) {
			if (arrayList.size() > 0) {
				for (ListIterator<Object> iterator = arrayList.listIterator(); iterator
						.hasNext();) {
					Object object = (Object) iterator.next();

					if (object == null) {
						if (value == null)
							iterator.remove();
					} else if (value != null) {
						String className = object.getClass().getSimpleName();
						if (className.equals("String")) {
							String stringObject = (String) object;
							if (stringObject.equalsIgnoreCase(value))
								iterator.remove();
						} else if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> nestedLinkedHashMap = (LinkedHashMap<String, Object>) object;
							iterator.set(removeAllObjectsFromLinkedHashMap(
									value, nestedLinkedHashMap));
						} else if (className.equals("ArrayList")) {
							ArrayList<Object> nestedArrayList = (ArrayList<Object>) object;
							iterator.set(removeAllObjectsFromArrayList(value,
									nestedArrayList));
						}
					}
				}
			}
		}

		return arrayList;
	}

	public LinkedHashMap<String, Object> updateAllObjectsFromLinkedHashMap(
			String oldValue, String newValue,
			LinkedHashMap<String, Object> srcLinkedHashMap) {
		if (srcLinkedHashMap != null) {
			if (srcLinkedHashMap.size() > 0) {
				Set<Entry<String, Object>> entrySet = (Set<Entry<String, Object>>) srcLinkedHashMap
						.entrySet(); // Entry Set
				for (Iterator<Entry<String, Object>> iterator = entrySet
						.iterator(); iterator.hasNext();) {
					Entry<String, Object> entry = iterator.next();

					Object object = entry.getValue();

					if (object == null) {
						if (oldValue == null)
							entry.setValue(newValue);
					} else if (oldValue != null) {
						String className = object.getClass().getSimpleName();
						if (className.equals("String")) {
							String stringObject = (String) object;
							if (stringObject.equalsIgnoreCase(oldValue))
								entry.setValue(newValue);
						} else if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> nestedLinkedHashMap = (LinkedHashMap<String, Object>) object;
							entry.setValue(updateAllObjectsFromLinkedHashMap(
									oldValue, newValue, nestedLinkedHashMap));
						} else if (className.equals("ArrayList")) {
							ArrayList<Object> nestedArrayList = (ArrayList<Object>) object;
							entry.setValue(updateAllObjectsFromArrayList(
									oldValue, newValue, nestedArrayList));
						}
					}
				}
			}
		}
		return srcLinkedHashMap;
	}

	public ArrayList<Object> updateAllObjectsFromArrayList(String oldValue,
			String newValue, ArrayList<Object> arrayList) {
		if (arrayList != null) {
			if (arrayList.size() > 0) {
				for (ListIterator<Object> iterator = arrayList.listIterator(); iterator
						.hasNext();) {
					Object object = (Object) iterator.next();

					if (object == null) {
						if (oldValue == null)
							iterator.set(newValue);
					} else if (oldValue != null) {
						String className = object.getClass().getSimpleName();
						if (className.equals("String")) {
							String stringObject = (String) object;
							if (stringObject.equalsIgnoreCase(oldValue))
								iterator.set(newValue);
						} else if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> nestedLinkedHashMap = (LinkedHashMap<String, Object>) object;
							iterator.set(updateAllObjectsFromLinkedHashMap(
									oldValue, newValue, nestedLinkedHashMap));
						} else if (className.equals("ArrayList")) {
							ArrayList<Object> nestedArrayList = (ArrayList<Object>) object;
							iterator.set(updateAllObjectsFromArrayList(
									oldValue, newValue, nestedArrayList));
						}
					}
				}
			}
		}

		return arrayList;
	}

	public LinkedHashMap<String, Object> removeAllKeysFromLinkedHashMap(
			String key, LinkedHashMap<String, Object> srcLinkedHashMap) {
		if (srcLinkedHashMap != null) {
			if (srcLinkedHashMap.size() > 0 && key != null) {
				if (srcLinkedHashMap.containsKey(key))
					srcLinkedHashMap.remove(key);

				Set<Entry<String, Object>> entrySet = (Set<Entry<String, Object>>) srcLinkedHashMap
						.entrySet(); // Entry Set
				for (Iterator<Entry<String, Object>> iterator = entrySet
						.iterator(); iterator.hasNext();) {
					Entry<String, Object> entry = iterator.next();

					Object object = entry.getKey();

					if (object != null) {
						String className = object.getClass().getSimpleName();
						if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> nestedLinkedHashMap = (LinkedHashMap<String, Object>) object;
							entry.setValue(removeAllKeysFromLinkedHashMap(key,nestedLinkedHashMap));
						} else if (className.equals("ArrayList")) {
							ArrayList<Object> nestedArrayList = (ArrayList<Object>) object;
							entry.setValue(removeAllKeysFromArrayList(key,nestedArrayList));
						}
					}
				}
			}
		}

		return srcLinkedHashMap;
	}

	private ArrayList<Object> removeAllKeysFromArrayList(String key,
			ArrayList<Object> arrayList) {
		if (arrayList != null) {
			if (arrayList.size() > 0 && key != null) {
				for (ListIterator<Object> iterator = arrayList.listIterator(); iterator.hasNext();) {
					Object object = (Object) iterator.next();

					if (object != null) {
						String className = object.getClass().getSimpleName();
						if (className.equals("LinkedHashMap")) {
							LinkedHashMap<String, Object> nestedLinkedHashMap = (LinkedHashMap<String, Object>) object;
							iterator.set(removeAllKeysFromLinkedHashMap(key,nestedLinkedHashMap));
						} else if (className.equals("ArrayList")) {
							ArrayList<Object> nestedArrayList = (ArrayList<Object>) object;
							iterator.set(removeAllKeysFromArrayList(key,nestedArrayList));
						}
					}
				}
			}
		}

		return arrayList;
	}

	public interface JSONParsingListener {
		public void onJSONParsingFinished(ArrayList<Object> jsonData,
				boolean isParsingSuccessful);
	}

}
