import { useState } from "react"
import { View, Text, StyleSheet, TouchableOpacity, ScrollView } from "react-native"
import { ThemedView } from "@/components/ThemedView"
import { postDrugInteractions } from "@/api/drug-interactions"
import { useAuth } from "@/context/auth-context" // Assumes you have an AuthContext setup

export default function DrugInteractions() {
    const [interactions, setInteractions] = useState<any[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState("")
    const { user } = useAuth()

    const handleCheck = async () => {
        if (!user) {
            setError("User not authenticated.")
            return
        }

        setLoading(true)
        setError("")
        try {
            const res = await postDrugInteractions(user.uid)
            setInteractions(res.interactions)
        } catch (err) {
            setError("Failed to check interactions.")
        } finally {
            setLoading(false)
        }
    }

    return (
        <ThemedView style={styles.container}>
            <Text style={styles.title}>Check for Drug Interactions</Text>

            <TouchableOpacity style={styles.button} onPress={handleCheck} disabled={loading}>
                <Text style={styles.buttonText}>Check Now</Text>
            </TouchableOpacity>

            {loading && <Text>Loading...</Text>}
            {error !== "" && <Text style={styles.error}>{error}</Text>}

            <ScrollView style={styles.results}>
                {interactions.length === 0 && !loading && (
                    <Text style={styles.okay}>No known severe interactions. You're good to go!</Text>
                )}
                {interactions
                    .filter((interaction) => interaction.level.toLowerCase() !== "unknown")
                    .map((interaction, index) => (
                        <View key={index} style={styles.resultBox}>
                            <Text style={styles.resultText}>
                                 <Text style={{ fontWeight: "bold" }}>{interaction.drugA}</Text> +{" "}
                                <Text style={{ fontWeight: "bold" }}>{interaction.drugB}</Text> —{" "}
                                <Text style={{ color: "red" }}>{interaction.level}</Text> interaction.
                            </Text>
                            <Text>Please consult your medical provider.</Text>
                        </View>
                    ))}
            </ScrollView>
        </ThemedView>
    )
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        padding: 20,
        gap: 16,
    },
    title: {
        fontSize: 24,
        fontWeight: "bold",
        color: "#333",
        textAlign: "center",
    },
    button: {
        backgroundColor: "#0066cc",
        paddingVertical: 12,
        paddingHorizontal: 24,
        borderRadius: 8,
        alignSelf: "center",
    },
    buttonText: {
        color: "#fff",
        fontSize: 16,
    },
    results: {
        marginTop: 20,
    },
    resultBox: {
        backgroundColor: "#fce4e4",
        padding: 12,
        marginVertical: 6,
        borderRadius: 6,
    },
    resultText: {
        fontSize: 16,
    },
    okay: {
        textAlign: "center",
        fontSize: 16,
        color: "green",
    },
    error: {
        color: "red",
        textAlign: "center",
    },
})
