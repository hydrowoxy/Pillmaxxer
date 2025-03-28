import { router, Link } from "expo-router";
import { Text, TextInput, View, Pressable, StyleSheet } from "react-native";
import { useState } from "react";
import { useAuth } from "@/context/auth-context";

/*
 * TODO - fix the actual UI
 */

/**
 * SignIn component handles user authentication through email and password
 */
export default function SignIn() {

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { signIn } = useAuth();

  const handleLogin = async () => {
    try {
      return await signIn(email, password);
    } catch (err) {
      console.log("[handleLogin] ==>", err);
      return null;
    }
  };

  /**
   * Handles the sign-in button press
   */
  const handleSignInPress = async () => {
    const resp = await handleLogin();
    if (resp) { // Check if login was successful before navigating
      router.replace("/(tabs)");
    } else {
        console.log("Login failed!");
    }
  };

  return (
    <View style={styles.container}>
      {/* Welcome Section */}
      <View style={styles.welcomeContainer}>
        <Text style={styles.welcomeTitle}>Welcome Back</Text>
        <Text style={styles.welcomeSubtitle}>Please sign in to continue</Text>
      </View>

      {/* Form Section */}
      <View style={styles.formContainer}>
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
          <Text style={styles.label}>Password</Text>
          <TextInput
            placeholder="Your password"
            value={password}
            onChangeText={setPassword}
            secureTextEntry
            textContentType="password"
            style={styles.input}
          />
        </View>
      </View>

      {/* Sign In Button */}
      <Pressable
        onPress={handleSignInPress}
        style={styles.signInButton}
      >
        <Text style={styles.signInButtonText}>Sign In</Text>
      </Pressable>

      {/* Sign Up Link */}
      <View style={styles.signUpContainer}>
        <Text style={styles.signUpText}>Don't have an account?</Text>
        <Link href="./signup" asChild>
          <Pressable style={styles.signUpLink}>
            <Text style={styles.signUpLinkText}>Sign Up</Text>
          </Pressable>
        </Link>
      </View>
    </View>
  );
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
    fontSize: 24,
    fontWeight: "bold",
    color: "#333", // Example color
    marginBottom: 10,
  },
  welcomeSubtitle: {
    fontSize: 16,
    color: "#666", // Example color
  },
  formContainer: {
    width: "100%",
    maxWidth: 300,
    marginBottom: 20,
  },
  label: {
    fontSize: 14,
    fontWeight: "medium",
    color: "#555",
    marginBottom: 5,
    marginLeft: 2,
  },
  input: {
    width: "100%",
    padding: 10,
    borderWidth: 1,
    borderColor: "#ccc",
    borderRadius: 8,
    fontSize: 16,
    backgroundColor: "#fff",
  },
  signInButton: {
    backgroundColor: "#007bff", // Example color
    width: "100%",
    maxWidth: 300,
    padding: 12,
    borderRadius: 8,
  },
  signInButtonText: {
    color: "#fff",
    fontWeight: "semibold",
    fontSize: 16,
    textAlign: "center",
  },
  signUpContainer: {
    flexDirection: "row",
    alignItems: "center",
    marginTop: 15,
  },
  signUpText: {
    color: "#777",
  },
  signUpLink: {
    marginLeft: 5,
  },
  signUpLinkText: {
    color: "#007bff", // Example color
    fontWeight: "semibold",
  },
});