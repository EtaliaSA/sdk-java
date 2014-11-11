package net.etalia.client.json;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.aspectj.lang.reflect.MethodSignature;

import net.etalia.client.domain.Entity;
import net.etalia.jalia.annotations.JsonIgnore;

public aspect JsonEntity {

	declare error : get(List Entity+.*) && !(withincode(* Entity+.get*(..))) && !(withincode(* Entity+.set*(..))) : "Use getters to use collections!";
	declare error : get(Set Entity+.*) && !(withincode(* Entity+.get*(..))) && !(withincode(* Entity+.set*(..))) : "Use getters to use collections!";
	declare error : get(Map Entity+.*) && !(withincode(* Entity+.get*(..))) && !(withincode(* Entity+.set*(..))) : "Use getters to use collections!";
	
	private Set<String> Entity.jsonUsedFields = new HashSet<String>();
	
	@JsonIgnore
	public Set<String> Entity.getJsonUsedFields() {
		return this.jsonUsedFields;
	}
	
	public void Entity.cleanJsonUsedFields() {
		this.getJsonUsedFields().clear();
	}
	
	
	after(Entity e) :
		execution(* Entity+.set*(..))
		&& this(e)
	{
		String name = ((MethodSignature)thisJoinPointStaticPart.getSignature()).getMethod().getName();
		name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
		if (name.equals("id")) return;
		e.getJsonUsedFields().add(name);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	Set around(Entity e) :
		execution(Set Entity+.get*(..))
		&& this(e)
		&& !(execution(Set Entity+.getJsonUsedFields()))
	{
		String name = ((MethodSignature)thisJoinPointStaticPart.getSignature()).getMethod().getName();
		name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
		Set val = proceed(e); 
		if (!(val instanceof MonitoredSet)) val = new MonitoredSet(e, name, val);
		return val;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	List around(Entity e) :
		execution(List Entity+.get*(..))
		&& this(e)
	{
		String name = ((MethodSignature)thisJoinPointStaticPart.getSignature()).getMethod().getName();
		name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
		List val = proceed(e); 
		if (!(val instanceof MonitoredList)) val = new MonitoredList(e, name, val);
		return val;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	Map around(Entity e) :
		execution(Map Entity+.get*(..))
		&& this(e)
	{
		String name = ((MethodSignature)thisJoinPointStaticPart.getSignature()).getMethod().getName();
		name = Character.toLowerCase(name.charAt(3)) + name.substring(4);
		Map val = proceed(e); 
		if (!(val instanceof MonitoredMap)) val = new MonitoredMap(e, name, val);
		return val;
	}

	public class MonitoredBase {

		protected Entity owner;
		protected String name;
		
		public MonitoredBase(Entity owner, String name) {
			this.owner = owner;
			this.name = name;
		}

		protected void modified() {
			this.owner.getJsonUsedFields().add(name);
		}
		
	}
	
	public class MonitoredIterator<T> extends MonitoredBase implements Iterator<T> {

		private Iterator<T> delegate;

		public MonitoredIterator(Entity owner, String name, Iterator<T> delegate) {
			super(owner, name);
			this.delegate = delegate;
		}

		public boolean hasNext() {
			return delegate.hasNext();
		}

		public T next() {
			return delegate.next();
		}

		public void remove() {
			modified();
			delegate.remove();
		}
		
	}
	
	public class MonitoredListIterator<T> extends MonitoredIterator<T> implements ListIterator<T> {

		private ListIterator<T> delegate;

		public MonitoredListIterator(Entity owner, String name, ListIterator<T> delegate) {
			super(owner, name, delegate);
			this.delegate = delegate;
		}

		public boolean hasPrevious() {
			return delegate.hasPrevious();
		}

		public T previous() {
			return delegate.previous();
		}

		public int nextIndex() {
			return delegate.nextIndex();
		}

		public int previousIndex() {
			return delegate.previousIndex();
		}

		public void set(T e) {
			modified();
			delegate.set(e);
		}

		public void add(T e) {
			modified();
			delegate.add(e);
		}
		
	}
	
	
	public class MonitoredCollection<T> extends MonitoredBase implements Collection<T> {

		private Collection<T> delegate;

		public MonitoredCollection(Entity owner, String name, Collection<T> delegate) {
			super(owner, name);
			this.delegate = delegate;
		}

		public int size() {
			return delegate.size();
		}

		public boolean isEmpty() {
			return delegate.isEmpty();
		}

		public boolean contains(Object o) {
			return delegate.contains(o);
		}

		public Iterator<T> iterator() {
			return new MonitoredIterator<T>(owner, name, delegate.iterator());
		}

		public Object[] toArray() {
			return delegate.toArray();
		}

		public <T> T[] toArray(T[] a) {
			return delegate.toArray(a);
		}

		public boolean add(T e) {
			modified();
			return delegate.add(e);
		}

		public boolean remove(Object o) {
			modified();
			return delegate.remove(o);
		}

		public boolean containsAll(Collection<?> c) {
			return delegate.containsAll(c);
		}

		public boolean addAll(Collection<? extends T> c) {
			modified();
			return delegate.addAll(c);
		}

		public boolean removeAll(Collection<?> c) {
			modified();
			return delegate.removeAll(c);
		}

		public boolean retainAll(Collection<?> c) {
			modified();
			return delegate.retainAll(c);
		}

		public void clear() {
			modified();
			delegate.clear();
		}

		public boolean equals(Object o) {
			return delegate.equals(o);
		}

		public int hashCode() {
			return delegate.hashCode();
		}
		
	}
	
	public class MonitoredSet<T> extends MonitoredCollection<T> implements Set<T> {

		protected Set<T> delegate;
		
		public MonitoredSet(Entity owner, String name, Set<T> delegate) {
			super(owner,name,delegate);
			this.delegate = delegate;
		}

	}
	
	public class MonitoredList<T> extends MonitoredCollection<T> implements List<T> {

		private List<T> delegate;

		public MonitoredList(Entity owner, String name, List<T> delegate) {
			super(owner, name, delegate);
			this.delegate = delegate;
		}

		public boolean addAll(int index, Collection<? extends T> c) {
			modified();
			return delegate.addAll(index, c);
		}

		public T get(int index) {
			return delegate.get(index);
		}

		public T set(int index, T element) {
			modified();
			return delegate.set(index, element);
		}

		public void add(int index, T element) {
			modified();
			delegate.add(index, element);
		}

		public T remove(int index) {
			modified();
			return delegate.remove(index);
		}

		public int indexOf(Object o) {
			return delegate.indexOf(o);
		}

		public int lastIndexOf(Object o) {
			return delegate.lastIndexOf(o);
		}

		public ListIterator<T> listIterator() {
			return new MonitoredListIterator<T>(owner, name, delegate.listIterator());
		}

		public ListIterator<T> listIterator(int index) {
			return new MonitoredListIterator<T>(owner, name, delegate.listIterator(index));
		}

		public List<T> subList(int fromIndex, int toIndex) {
			return new MonitoredList<T>(this.owner, this.name, delegate.subList(fromIndex, toIndex));
		}
		
	}
	
	public class MonitoredMap<K,V> extends MonitoredBase implements Map<K,V> {

		private Map<K, V> delegate;

		public MonitoredMap(Entity owner, String name, Map<K,V> delegate) {
			super(owner, name + ".*");
			this.delegate = delegate;
		}

		public int size() {
			return delegate.size();
		}

		public boolean isEmpty() {
			return delegate.isEmpty();
		}

		public boolean containsKey(Object key) {
			return delegate.containsKey(key);
		}

		public boolean containsValue(Object value) {
			return delegate.containsValue(value);
		}

		public V get(Object key) {
			return delegate.get(key);
		}

		public V put(K key, V value) {
			modified();
			return delegate.put(key, value);
		}

		public V remove(Object key) {
			modified();
			return delegate.remove(key);
		}

		public void putAll(Map<? extends K, ? extends V> m) {
			modified();
			delegate.putAll(m);
		}

		public void clear() {
			modified();
			delegate.clear();
		}

		public Set<K> keySet() {
			return new MonitoredSet<K>(owner, name, delegate.keySet());
		}

		public Collection<V> values() {
			return new MonitoredCollection<V>(owner,name,delegate.values());
		}

		public Set<java.util.Map.Entry<K, V>> entrySet() {
			return new MonitoredSet<Entry<K,V>>(owner,name,delegate.entrySet());
		}

		public boolean equals(Object o) {
			return delegate.equals(o);
		}

		public int hashCode() {
			return delegate.hashCode();
		}
		
	}
	
}
