from Crypto.Cipher import AES
import hashlib
import base64

def decrypt_aes(encrypted_text, key):
    fixed_iv = b"1234567890123456"
    cipher = AES.new(key, AES.MODE_CBC, fixed_iv)
    decrypted_bytes = cipher.decrypt(base64.b64decode(encrypted_text))
    return decrypted_bytes.rstrip(b"\x00").decode("utf-8")

string1 = "secret123"
string2 = "part2"
aes_key = hashlib.sha256((string1 + string2).encode()).digest()
encrypted_pass = "cRnMG0hxrFlwT6me9nTW3u24okjQ9oj1voufjKKE3rvrpBVC40WG8aViRlko/jEd"

print("Decrypted Password:", decrypt_aes(encrypted_pass, aes_key))