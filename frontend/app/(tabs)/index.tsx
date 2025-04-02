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
        <Text style={styles.buttonText}>Start with a photo.</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.button}
        onPress={() => {
          router.push("/(tabs)/prescription-form")
        }}
      >
        <Text style={styles.buttonText}>Start with a form.</Text>
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
    gap: 8,
  },
  title: {
    fontSize: 45,
    fontWeight: "700",
    color: "#FFF",
    textTransform: "uppercase",
    marginBottom: 8,
  },
  subtitle: {
    color: "#FFF",
    marginBottom: 28,
    textAlign: "center",
  },
  button: {
    paddingVertical: 12,
    paddingHorizontal: 30,
    backgroundColor: "#FFF",
    borderRadius: 30,
  },
  buttonText: {
    fontWeight: "600",
    color: "#156fe9",
    textAlign: "center",
  },
})
