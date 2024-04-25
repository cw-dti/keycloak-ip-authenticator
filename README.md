# Keycloak-IP-Authenticator

Ein einfacher Keycloak Authenticator, der die IP-Adresse des Clients überprüft. Die IP Adressen
könnten dazu in den Attributen einer Gruppe hinterlegt werden. Die Gruppe muss dabei das
Prefix `IPX_` nutzen, um von dem Authenticator erkannt zu werden.

In der Gruppe könnten die Attribute `IPX_RANGE` und `IPX_RANGE_URL` genutzt werden um die
IP-Adressen
zu definieren. Die IP-Adressen müssen dabei im CIDR Format angegeben werden.

In `IPX_RANGE` kann eine oder mehrere Adressen mit einem Komma oder Semikolon getrennt hinterlegt
werden.
> Beispiel: `192.168.232.91/32, 129.168.232.92/32`

In `IPX_RANGE_URL` kann eine URL hinterlegt werden, die auf eine File mit IPs verweist. Es wird
dabei von
einer IP-Adresse pro Zeile ausgegangen.

Beide Attribute könnten mehrfach und zusammen verwendet werden, die IP-Adressen werden dabei
zusammengefasst.

## Build

Stellen Sie immer sicher, dass die Keycloak SPI-Abhängigkeiten und die Versionen Ihres
Keycloak-Servers übereinstimmen. Die Version der Keycloak SPI-Abhängigkeiten ist in der `pom.xml`
im `keycloak.version`-Eigenschaft konfiguriert.

Um das Projekt zu bauen, führen Sie den folgenden Befehl aus:

```bash
mvn package
```

## Deploy

Davon ausgehend das `$KEYCLOAK_HOME` auf Ihr Keycloak-Installationsverzeichnis zeigt.

Wenn Sie ein Legacy Keycloak auf WildFly verwenden, kopieren Sie es in das
Bereitstellungsverzeichnis:

```bash
cp target/keycloak-ip-authenticator.jar $KEYCLOAK_HOME/standalone/deployments/
```

Wenn Sie das neueste Keycloak auf Quarkus verwenden, kopieren Sie es in das Verzeichnis:

```bash
cp target/keycloak-ip-authenticator.jar $KEYCLOAK_HOME/providers/
```

# Nutzung

Nachdem Sie den Authenticator installiert haben, können Sie ihn in den Authentication Einstellungen
von Keycloak einen eigenen Login Flow (für den Browser) anlegen, der den Authenticator verwendet.