import { useEffect, useState } from "react"
import { Text, Image, StyleSheet, TouchableOpacity } from "react-native"
import * as ImagePicker from "expo-image-picker"
import { View } from "react-native"
import { router, Link } from "expo-router"
import { postImageUpload } from "../../api/image-scan"

export default function App() {
  const [image, setImage] = useState<string | null>(null)
  const [hasPermission, setHasPermission] = useState<boolean | null>(null)
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    ;(async () => {
      const { status } = await ImagePicker.requestCameraPermissionsAsync()
      setHasPermission(status === "granted")
    })()
  }, [])

  const takePhoto = async () => {
    if (!hasPermission) {
      alert("Permission to access camera is required")
      return
    }

    const result = await ImagePicker.launchCameraAsync()
    if (!result.canceled) {
      setImage(result.assets[0].uri)
    }
  }

  const handleImageUpload = async () => {
    setLoading(true)
    const res = await postImageUpload({ params: { imageFile: image || "" } })
    setLoading(false)
    router.replace("/(tabs)") // TODO: implement redirecting to frontend form and auto-filling as much data as possible from res
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Scan your prescription.</Text>

      {image && <Image source={{ uri: image }} style={styles.image} />}

      <TouchableOpacity
        style={[styles.button, loading && styles.buttonDisabled]}
        onPress={takePhoto}
      >
        <Text style={styles.buttonText}>
          {image ? "I want to take a better photo." : "I want to take a photo."}
        </Text>
      </TouchableOpacity>

      {image && (
        <TouchableOpacity
          style={[styles.button, loading && styles.buttonDisabled]}
          onPress={handleImageUpload}
          disabled={loading}
        >
          <Text style={styles.buttonText}>I'm happy with this photo!</Text>
        </TouchableOpacity>
      )}

      {loading && <Text>Loading...</Text>}

      <Link href="/" style={styles.link}>
        Take me back home, please.
      </Link>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "#f9f9f9",
    padding: 20,
    gap: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: "600",
    color: "#333",
  },
  button: {
    backgroundColor: "#0066cc",
    paddingVertical: 12,
    paddingHorizontal: 24,
    borderRadius: 8,
  },
  buttonDisabled: {
    backgroundColor: "#99ccff",
  },
  buttonText: {
    color: "#fff",
    fontSize: 14,
    fontWeight: "500",
  },
  image: {
    width: 250,
    height: 250,
    borderRadius: 12,
    marginBottom: 20,
    borderColor: "#ddd",
    resizeMode: "cover",
  },
  link: {
    color: "#0066cc",
    fontSize: 14,
    textDecorationLine: "underline",
  },
})
