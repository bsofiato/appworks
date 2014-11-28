/*
 * HashTypeMappingTest.java
 * JUnit based test
 *
 * Created on 9 de Agosto de 2005, 23:29
 */

package br.com.appworks.runtime.lang.support.test;

import br.com.appworks.runtime.lang.support.HashTypeMap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import junit.framework.*;

/**
 *
 * @author Bubu
 */
public class HashTypeMapTest extends TestCase {
  
  public HashTypeMapTest(String testName) {
    super(testName);
  }

  protected void setUp() throws Exception {
  }

  protected void tearDown() throws Exception {
  }
  
  private <Type> HashTypeMap <Type> serialize(HashTypeMap <Type> map) {
    ByteArrayOutputStream baos = null;
    ObjectOutputStream oos = null;
    ByteArrayInputStream bais = null;
    ObjectInputStream ois = null;
    try {
      baos = new ByteArrayOutputStream();
      oos = new ObjectOutputStream(baos);
      oos.writeObject(map);
      oos.flush();
      bais = new ByteArrayInputStream(baos.toByteArray());
      ois = new ObjectInputStream(bais);
      return (HashTypeMap <Type>)(ois.readObject());
    } catch (Exception ex) {
      ex.printStackTrace();
      fail();
    } finally {
      try {
        baos.close();
        oos.close();
      } catch (Exception ex) {
        fail();
      }
    }
    return null;
  }
  
   public void testNonEmptyHashTypeMapSerialization() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    hashTypeMapping.put(Map.class, "TESTE");
    HashTypeMap <String> deserializedTypeMapping = serialize(hashTypeMapping);
    assertEquals(hashTypeMapping, deserializedTypeMapping);
    assertEquals("TESTE", deserializedTypeMapping.get(Map.class));
    assertEquals("TESTE", deserializedTypeMapping.get(HashMap.class));
    assertNull(deserializedTypeMapping.get(String.class));
  }
  
  public void testEmptyHashTypeMapSerialization() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    HashTypeMap <String> deserializedTypeMapping = serialize(hashTypeMapping);
    assertEquals(hashTypeMapping, deserializedTypeMapping);
    deserializedTypeMapping.put(Map.class, "TESTE");
    assertEquals("TESTE", deserializedTypeMapping.get(Map.class));
    assertEquals("TESTE", deserializedTypeMapping.get(HashMap.class));
    assertNull(deserializedTypeMapping.get(String.class));
  }
  public void testNullParameterConstructor() {
    try {
      new HashTypeMap <String> (null);
      fail();
    } catch (NullPointerException ex) {
    }    
  }

  public void testMapParameterConstructor() {
    Map <Class <? extends Object>, String> map = new HashMap <Class <? extends Object>, String> ();
    map.put(Map.class, "TESTE");
    map.put(String.class, "TESTE2");
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> (map);
    assertTrue(hashTypeMapping.containsKey(Map.class));
    assertTrue(hashTypeMapping.containsKey(String.class));
    assertTrue(hashTypeMapping.containsKey(HashMap.class));
    assertTrue(hashTypeMapping.containsValue("TESTE"));
    assertTrue(hashTypeMapping.containsValue("TESTE2"));
    assertEquals("TESTE", hashTypeMapping.get(Map.class));
    assertEquals("TESTE", hashTypeMapping.get(HashMap.class));
    assertEquals("TESTE2", hashTypeMapping.get(String.class));
    assertEquals(2, hashTypeMapping.size());
    assertFalse(hashTypeMapping.isEmpty());
  }

  public void testEntrySet() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertTrue(hashTypeMapping.entrySet().isEmpty());
    hashTypeMapping.put(Map.class, "TESTE");
    assertFalse(hashTypeMapping.entrySet().isEmpty());
    assertEquals(1, hashTypeMapping.entrySet().size());
    for (Map.Entry <Class, String> entry : hashTypeMapping.entrySet()) {
      if (Map.class.equals(entry.getKey())) {
        assertEquals("TESTE", entry.getValue());
      } else {
        fail();
      }
    }
  }

  public void testValues() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertTrue(hashTypeMapping.values().isEmpty());
    hashTypeMapping.put(Map.class, "TESTE");
    assertFalse(hashTypeMapping.values().isEmpty());
    assertEquals(1, hashTypeMapping.values().size());
    assertTrue(hashTypeMapping.values().contains("TESTE"));    
  }

  public void testKeySet() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertTrue(hashTypeMapping.keySet().isEmpty());
    hashTypeMapping.put(Map.class, "TESTE");
    assertFalse(hashTypeMapping.keySet().isEmpty());
    assertEquals(1, hashTypeMapping.keySet().size());
    assertTrue(hashTypeMapping.keySet().contains(Map.class));
  }
  public void testPutAll() {
    Map <Class <? extends Object>, String> map = new HashMap <Class <? extends Object>, String> ();
    map.put(Map.class, "TESTE");
    map.put(String.class, "TESTE2");
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    hashTypeMapping.putAll(map);
    assertTrue(hashTypeMapping.containsKey(Map.class));
    assertTrue(hashTypeMapping.containsKey(String.class));
    assertTrue(hashTypeMapping.containsKey(HashMap.class));
    assertTrue(hashTypeMapping.containsValue("TESTE"));
    assertTrue(hashTypeMapping.containsValue("TESTE2"));
    assertEquals("TESTE", hashTypeMapping.get(Map.class));
    assertEquals("TESTE", hashTypeMapping.get(HashMap.class));
    assertEquals("TESTE2", hashTypeMapping.get(String.class));
    assertEquals(2, hashTypeMapping.size());
    assertFalse(hashTypeMapping.isEmpty());
  }

  public void testFilledMapPutAll() {
    Map <Class <? extends Object>, String> map = new HashMap <Class <? extends Object>, String> ();
    map.put(Map.class, "TESTE");
    map.put(String.class, "TESTE2");
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    hashTypeMapping.put(Integer.class, "TESTE3");
    hashTypeMapping.putAll(map);
    assertTrue(hashTypeMapping.containsKey(Map.class));
    assertTrue(hashTypeMapping.containsKey(String.class));
    assertTrue(hashTypeMapping.containsKey(HashMap.class));
    assertFalse(hashTypeMapping.containsKey(Integer.class));
    assertTrue(hashTypeMapping.containsValue("TESTE"));
    assertTrue(hashTypeMapping.containsValue("TESTE2"));
    assertFalse(hashTypeMapping.containsValue("TESTE3"));
    assertEquals("TESTE", hashTypeMapping.get(Map.class));
    assertEquals("TESTE", hashTypeMapping.get(HashMap.class));
    assertEquals("TESTE2", hashTypeMapping.get(String.class));
    assertNull(hashTypeMapping.get(Integer.class));
    assertEquals(2, hashTypeMapping.size());
    assertFalse(hashTypeMapping.isEmpty());
  }

  public void testNullMapPutAll() {
    try {
      new HashTypeMap <String> ().putAll(null);
      fail();
    } catch (NullPointerException ex) {
    }
  }
  public void testExistantClassRemove() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    hashTypeMapping.put(Map.class, "TESTE");
    hashTypeMapping.remove(Map.class);
    assertNull(hashTypeMapping.get(HashMap.class));
    assertFalse(hashTypeMapping.containsKey(HashMap.class));
    assertNull(hashTypeMapping.get(Map.class));
    assertFalse(hashTypeMapping.containsKey(Map.class));
    assertFalse(hashTypeMapping.containsValue("TESTE"));
    assertEquals(hashTypeMapping, new HashTypeMap <String> ());
    assertTrue(hashTypeMapping.isEmpty());
    assertEquals(0, hashTypeMapping.size());
  }

  public void testNonExistantClassRemove() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    hashTypeMapping.put(Map.class, "TESTE");
    hashTypeMapping.remove(HashMap.class);
    assertEquals("TESTE", hashTypeMapping.get(HashMap.class));
    assertTrue(hashTypeMapping.containsKey(HashMap.class));
    assertEquals("TESTE", hashTypeMapping.get(Map.class));
    assertTrue(hashTypeMapping.containsKey(Map.class));
    assertTrue(hashTypeMapping.containsValue("TESTE"));
    assertFalse(hashTypeMapping.isEmpty());
    assertEquals(1, hashTypeMapping.size());
  }

  public void testNullValuePut() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertNull(hashTypeMapping.put(Map.class, null));
    assertNull(hashTypeMapping.get(HashMap.class));
    assertTrue(hashTypeMapping.containsKey(Map.class));
    assertTrue(hashTypeMapping.containsKey(HashMap.class));
  }
  
  public void testPut() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertNull(hashTypeMapping.put(Map.class, "TESTE"));
    assertEquals("TESTE", hashTypeMapping.get(HashMap.class));
    assertTrue(hashTypeMapping.containsKey(HashMap.class));
    assertEquals("TESTE", hashTypeMapping.get(Map.class));
    assertTrue(hashTypeMapping.containsKey(Map.class));
    assertTrue(hashTypeMapping.containsValue("TESTE"));
    assertFalse(hashTypeMapping.isEmpty());
    assertEquals(1, hashTypeMapping.size());
    assertEquals("TESTE", hashTypeMapping.put(Map.class, "TESTE2"));
    assertEquals("TESTE2", hashTypeMapping.get(HashMap.class));
    assertEquals("TESTE2", hashTypeMapping.get(HashMap.class));
    assertTrue(hashTypeMapping.containsKey(HashMap.class));
    assertEquals("TESTE2", hashTypeMapping.get(Map.class));
    assertTrue(hashTypeMapping.containsKey(Map.class));
    assertFalse(hashTypeMapping.containsValue("TESTE"));
    assertTrue(hashTypeMapping.containsValue("TESTE2"));
    assertFalse(hashTypeMapping.isEmpty());
    assertEquals(1, hashTypeMapping.size());
  }

  public void testGet() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertNull(hashTypeMapping.get(Map.class));
    hashTypeMapping.put(Map.class, "TESTE");
    assertEquals("TESTE", hashTypeMapping.get(Map.class));
    assertEquals("TESTE", hashTypeMapping.get(SortedMap.class));
    hashTypeMapping.put(SortedMap.class, "TESTE2");
    assertEquals("TESTE", hashTypeMapping.get(Map.class));
    assertEquals("TESTE2", hashTypeMapping.get(SortedMap.class));
    assertEquals("TESTE2", hashTypeMapping.get(TreeMap.class));
    hashTypeMapping.put(TreeMap.class, "TESTE3");
    assertEquals("TESTE", hashTypeMapping.get(Map.class));
    assertEquals("TESTE2", hashTypeMapping.get(SortedMap.class));
    assertEquals("TESTE3", hashTypeMapping.get(TreeMap.class));
    assertNull(hashTypeMapping.get(String.class));
  }
  public void testContainsValue() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertFalse(hashTypeMapping.containsValue("TESTE"));
    hashTypeMapping.put(Map.class, "TESTE");
    assertTrue(hashTypeMapping.containsValue("TESTE"));
    assertFalse(hashTypeMapping.containsValue("TESTE1"));
  }

  public void testContainsKey() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertFalse(hashTypeMapping.containsKey(Map.class));
    hashTypeMapping.put(Map.class, "TESTE");
    assertTrue(hashTypeMapping.containsKey(Map.class));
    assertTrue(hashTypeMapping.containsKey(HashMap.class));
    assertFalse(hashTypeMapping.containsKey(String.class));
  }
  
  public void testClear() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    hashTypeMapping.put(Map.class, "TESTE");
    hashTypeMapping.clear();
    assertNull(hashTypeMapping.get(HashMap.class));
    assertFalse(hashTypeMapping.containsKey(HashMap.class));
    assertNull(hashTypeMapping.get(Map.class));
    assertFalse(hashTypeMapping.containsKey(Map.class));
    assertFalse(hashTypeMapping.containsValue("TESTE"));
    assertEquals(hashTypeMapping, new HashTypeMap <String> ());
    assertTrue(hashTypeMapping.isEmpty());
    assertEquals(0, hashTypeMapping.size());
  }
  

  public void testSize() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertEquals(0, hashTypeMapping.size());
    hashTypeMapping.put(Map.class, "TESTE");
    assertEquals(1, hashTypeMapping.size());
    assertEquals("TESTE", hashTypeMapping.get(HashMap.class));
    assertEquals(1, hashTypeMapping.size());
  }
  
  public void testIsEmpty() {
    HashTypeMap <String> hashTypeMapping = new HashTypeMap <String> ();
    assertTrue(hashTypeMapping.isEmpty());
    hashTypeMapping.put(Map.class, "TESTE");
    assertFalse(hashTypeMapping.isEmpty());
  }
  
  public void testHashCode() {
    HashTypeMap <String> op1 = new HashTypeMap <String> ();
    op1.put(Map.class, "TESTE");
    HashTypeMap <String> op2 = new HashTypeMap <String> ();
    op2.put(Map.class, "TESTE");
    assertEquals("TESTE", op2.get(HashMap.class));
    assertEquals(op1.hashCode(), op2.hashCode());
  }

  public void testEquals() {
    HashTypeMap <String> op1 = new HashTypeMap <String> ();
    op1.put(Map.class, "TESTE");
    HashTypeMap <String> op2 = new HashTypeMap <String> ();
    op2.put(Map.class, "TESTE");
    assertEquals("TESTE", op2.get(HashMap.class));
    assertEquals(op1, op2);
  }
  

}
