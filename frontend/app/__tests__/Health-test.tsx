import { getHealth } from "@/api/general"

it("is able to access the server", async () => {
  const res = await getHealth()
  expect(res.status).toEqual("OK")
})
