import React from "react"
import {
  View,
  TouchableOpacity,
  Text,
  StyleSheet,
  ColorValue,
} from "react-native"
import { Ionicons } from "@expo/vector-icons"
import { useRouter } from "expo-router"

const ICON_COLOUR = "#156fe9" as ColorValue

const BottomBar: React.FC = () => {
  const router = useRouter()

  const handleButtonPress = (route: string) => {
    router.push(route as any)
  }

  return (
    <View style={styles.buttonContainer}>
      <TouchableOpacity
        style={styles.button}
        onPress={() => handleButtonPress("/")}
      >
        <Ionicons name="home" size={30} color={ICON_COLOUR} />
        <Text style={styles.buttonText}>Home</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.button}
        onPress={() => handleButtonPress("/medication-schedule")}
      >
        <Ionicons name="grid" size={30} color={ICON_COLOUR} />
        <Text style={styles.buttonText}>Schedule</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.button}
        onPress={() => handleButtonPress("/scan")}
      >
        <Ionicons name="camera" size={30} color={ICON_COLOUR} />
        <Text style={styles.buttonText}>Scan</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.button}
        onPress={() => handleButtonPress("/prescription-form")}
      >
        <Ionicons name="document-text" size={30} color={ICON_COLOUR} />
        <Text style={styles.buttonText}>Form</Text>
      </TouchableOpacity>

      <TouchableOpacity
        style={styles.button}
        onPress={() => handleButtonPress("/profile")}
      >
        <Ionicons name="person" size={30} color={ICON_COLOUR} />
        <Text style={styles.buttonText}>Profile</Text>
      </TouchableOpacity>
    </View>
  )
}

const styles = StyleSheet.create({
  buttonContainer: {
    flexDirection: "row",
    justifyContent: "space-around",
    marginTop: 12,
    marginBottom: 20,
  },
  button: {
    alignItems: "center",
    justifyContent: "center",
    color: "#156fe9",
    width: 64,
    height: 64,
  },
  buttonText: {
    color: "#156fe9",
    fontSize: 12,
    marginTop: 5,
  },
})

export default BottomBar
