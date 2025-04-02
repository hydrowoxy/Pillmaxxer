import { router, Link } from "expo-router"
import { Text, TextInput, View, Pressable, StyleSheet } from "react-native"
import { registerPatient } from "@/api/general"
import { useState } from "react"

/*
 * TODO - fix the actual UI
 */

export default function SignUp() {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  const [firstName, setFirstName] = useState("")
  const [lastName, setLastName] = useState("")
  const [phoneNumber, setPhoneNumber] = useState("")

  const handleRegister = async () => {
    try {
      // Register patient with backend (which will also create Firebase user)
      const patient = await registerPatient({
        email,
        password,
        firstName,
        lastName,
        phoneNumber,
        deviceToken: ["dummy-token"],
      })

      return patient
    } catch (err) {
      console.log("[handleRegister] ==>", err)
      return null
    }
  }

  const handleSignUpPress = async () => {
    const resp = await handleRegister()
    if (resp) {
      router.replace("/(tabs)")
    } else {
      console.log("Registration failed")
    }
  }

  return (
    <View style={styles.container}>
      {/* Welcome Section */}
      <View style={styles.welcomeContainer}>
        <Text style={styles.welcomeTitle}>Get started.</Text>
        <Text style={styles.welcomeSubtitle}>
          Can't pillmaxx without an account, buckaroo.
        </Text>
      </View>

      {/* Form Section */}
      <View style={styles.formContainer}>
        <View>
          <Text style={styles.label}>First Name</Text>
          <TextInput
            placeholder="John"
            value={firstName}
            onChangeText={setFirstName}
            textContentType="givenName"
            autoCapitalize="words"
            style={styles.input}
          />
        </View>

        <View>
          <Text style={styles.label}>Last Name</Text>
          <TextInput
            placeholder="Doe"
            value={lastName}
            onChangeText={setLastName}
            textContentType="familyName"
            autoCapitalize="words"
            style={styles.input}
          />
        </View>

        <View>
          <Text style={styles.label}>Email</Text>
          <TextInput
            placeholder="name@mail.com"
            value={email}
            onChangeText={setEmail}
            textContentType="emailAddress"
            keyboardType="email-address"
            autoCapitalize="none"
            style={styles.input}
          />
        </View>

        <View>
          <Text style={styles.label}>Phone Number</Text>
          <TextInput
            placeholder="+1234567890"
            value={phoneNumber}
            onChangeText={setPhoneNumber}
            textContentType="telephoneNumber"
            keyboardType="phone-pad"
            style={styles.input}
          />
        </View>

        <View>
          <Text style={styles.label}>Password</Text>
          <TextInput
            placeholder="Create a password"
            value={password}
            onChangeText={setPassword}
            secureTextEntry
            textContentType="newPassword"
            style={styles.input}
          />
        </View>
      </View>

      {/* Sign Up Button */}
      <Pressable onPress={handleSignUpPress} style={styles.signUpButton}>
        <Text style={styles.signUpButtonText}>Sign Up</Text>
      </Pressable>

      {/* Sign In Link */}
      <View style={styles.signInContainer}>
        <Text style={styles.signUpText}>Already have an account?</Text>
        <Link href="./login" asChild>
          <Pressable style={styles.signInLink}>
            <Text style={styles.signInLinkText}>Sign In</Text>
          </Pressable>
        </Link>
      </View>
    </View>
  )
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    padding: 20,
  },
  welcomeContainer: {
    alignItems: "center",
    marginBottom: 20,
  },
  welcomeTitle: {
    fontSize: 36,
    fontWeight: "600",
    color: "#333",
    marginBottom: 12,
  },
  welcomeSubtitle: {
    color: "#333",
    marginBottom: 24,
  },
  formContainer: {
    width: "100%",
    maxWidth: 300,
    marginBottom: 20,
  },
  label: {
    fontWeight: "bold",
    alignSelf: "flex-start",
  },
  input: {
    width: "100%",
    borderWidth: 1,
    padding: 8,
    borderColor: "#ddd",
    borderRadius: 8,
    marginBottom: 16,
    backgroundColor: "#fff",
  },
  signUpButton: {
    backgroundColor: "#156fe9",
    width: "100%",
    maxWidth: 300,
    padding: 12,
    borderRadius: 39,
  },
  signUpButtonText: {
    color: "#fff",
    fontWeight: "semibold",
    fontSize: 16,
    textAlign: "center",
  },
  signInContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginTop: 15,
  },
  signUpText: {
    color: "#777",
  },
  signInLink: {
    marginLeft: 5,
  },
  signInLinkText: {
    color: "#156fe9",
    fontWeight: "semibold",
  },
})
