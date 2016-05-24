#chronicle-map

* [Chronicle Map 2 has hard creation-time limit on the number of entries storable in a Chronicle Map. If the size exceeds this limit, an exception is thrown. In Chronicle Map 3, this limitation is removed, though the number of entries still has to be configured on the Chronicle Map creation, exceeding this configured limit is possible, but discouraged. See the Number of entries configuration section.](https://github.com/OpenHFT/Chronicle-Map#difference-between-chronicle-map-2-and-3)

###Chronicle Map is not

* A document store. No secondary indexes.
* A multimap. Using a ChronicleMap<K, Collection<V>> as multimap is technically possible, but often leads to problems (see a StackOverflow answer for details). Developing a proper multimap with Chronicle Map's design principles is possible, contact us is you consider sponsoring such development.

###Chronicle Map doesn't support

* Range queries, iteration over the entries in alphabetical order. Keys in Chronicle Map are not sorted.
* LRU entry eviction

