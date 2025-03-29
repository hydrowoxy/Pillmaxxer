import { Link } from "expo-router"
import { View, Text, StyleSheet, Button } from "react-native"

export default function HomeScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>We are ready to PillMaxx !</Text>

      <Link href="/scan-image">GO TO IMAGE SCAN TEST</Link>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#fff",
  },
  title: {
    fontSize: 40,
    fontWeight: "bold",
    color: "red",
    textTransform: "uppercase",
  },
})
