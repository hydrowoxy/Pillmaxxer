import { useEffect, useState } from "react"
import { Text, Image, StyleSheet, TouchableOpacity } from "react-native"
import * as ImagePicker from "expo-image-picker"
import { View } from "react-native"
import { router, Link } from "expo-router"
import { postImageUpload } from "../../api/image-scan"

export default function ScanImage() {
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
    console.log("processing complete")
    setLoading(false)

    router.setParams({ autofill: JSON.stringify(res) })
    router.push("/(tabs)/form")
  }

  return (
    <View style={styles.container}>
      <Text style={styles.title}>Scan your prescription.</Text>

      {image && <Image source={{ uri: image }} style={styles.image} />}

      {!hasPermission && <Text>Camera permission is required!</Text>}

      <TouchableOpacity
        style={[styles.button, loading && styles.buttonDisabled]}
        onPress={takePhoto}
        testID="take-image-button"
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
          testID="upload-image-button"
        >
          <Text style={styles.buttonText}>I'm happy with this photo!</Text>
        </TouchableOpacity>
      )}

      <Link href="/" style={styles.link}>
        Nah, take me back home.
      </Link>
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
    gap: 16,
  },
  title: {
    fontSize: 24,
    fontWeight: "700",
    color: "#FFF",
    marginBottom: 10,
  },
  button: {
    paddingVertical: 12,
    paddingHorizontal: 30,
    backgroundColor: "#FFF",
    borderRadius: 30,
  },
  buttonDisabled: {
    opacity: 0.5,
  },
  buttonText: {
    fontWeight: "600",
    color: "#156fe9",
    textAlign: "center",
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
    color: "#fff",
    fontSize: 14,
    textDecorationLine: "underline",
  },
})
