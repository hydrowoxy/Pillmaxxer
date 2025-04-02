import { router } from "expo-router"
import { View, Text, StyleSheet, TouchableOpacity } from "react-native"

export default function HomeScreen() {
  return (
    <View style={styles.container}>
      <Text style={styles.title}>PillMaxxer</Text>
      <Text style={styles.subtitle}>
        Imagine overdosing. Imagine underdosing. Honestly, kind of embarrassing.
        How about we track a new prescription together?
      </Text>
      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          router.push("/(tabs)/scan")
        }}
      >
        <Text style={styles.buttonText}>Let me take a photo.</Text>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#156fe9",
    padding: 20,
  },
  title: {
    fontSize: 45,
    fontWeight: "700",
    color: "#FFF",
    textTransform: "uppercase",
    marginBottom: 10,
  },
  subtitle: {
    color: "#FFF",
    marginBottom: 40,
    textAlign: "center",
  },
  button: {
    paddingVertical: 12,
    paddingHorizontal: 30,
    backgroundColor: "#FFF",
    borderRadius: 30,
  },
  buttonText: {
    fontSize: 18,
    fontWeight: "600",
    color: "#156fe9",
    textAlign: "center",
  },
})
