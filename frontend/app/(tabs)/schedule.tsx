import React, { useState, useCallback } from 'react';
import { View, Text, FlatList, StyleSheet, TouchableOpacity } from 'react-native';
import { useRouter, useFocusEffect } from 'expo-router';
import { scheduleItems } from '@/context/schedule-context';

interface ScheduleItem {
  id: string;
  title: string;
  time: string;
}

export default function Schedule() {
  const router = useRouter();
  const [items, setItems] = useState<ScheduleItem[]>([...scheduleItems]);

  useFocusEffect(
    useCallback(() => {
      console.log("Schedule screen focused. Current scheduleItems:", scheduleItems);
      setItems([...scheduleItems]); 
    }, [])
  );

  return (
    <View style={styles.container}>
      <View style={styles.headerContainer}>
        <Text style={styles.header}>Today's Schedule</Text>
        <TouchableOpacity
          style={styles.addButton}
          onPress={() => router.push('/(tabs)/form')}
        >
          <Text style={styles.addButtonText}>+</Text>
        </TouchableOpacity>
      </View>
      {items.length === 0 ? (
        <Text style={styles.emptyMessage}>No schedule items yet.</Text>
      ) : (
        <FlatList
          data={items}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <View style={styles.itemContainer}>
              <Text style={styles.itemTitle}>{item.title}</Text>
              <Text style={styles.itemTime}>{item.time}</Text>
            </View>
          )}
          contentContainerStyle={styles.listContainer}
        />
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    paddingHorizontal: 20,
    paddingTop: 50,
  },
  headerContainer: {
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
  },
  header: {
    fontSize: 28,
    fontWeight: 'bold',
  },
  addButton: {
    backgroundColor: '#007AFF',
    borderRadius: 20,
    width: 40,
    height: 40,
    justifyContent: 'center',
    alignItems: 'center',
  },
  addButtonText: {
    color: '#fff',
    fontSize: 24,
    lineHeight: 24,
  },
  emptyMessage: {
    fontSize: 18,
    color: '#555',
    marginTop: 20,
    textAlign: 'center',
  },
  listContainer: {
    paddingBottom: 20,
    marginTop: 20,
  },
  itemContainer: {
    backgroundColor: '#f0f0f0',
    padding: 15,
    borderRadius: 8,
    marginBottom: 15,
  },
  itemTitle: {
    fontSize: 18,
    fontWeight: '600',
  },
  itemTime: {
    fontSize: 16,
    color: '#555',
    marginTop: 5,
  },
});
