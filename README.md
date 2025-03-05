# EQUALIZEME APP e EQUALIZEME SERVICE
#### Visão Geral do Projeto

O projeto **EqualizeMe** consiste em um aplicativo Android (EqualizeMe App) e um serviço (EqualizeMe Service) que juntos permitem aos usuários ajustar os níveis de áudio do dispositivo. A comunicação entre o aplicativo e o serviço é feita através de uma interface AIDL que define métodos para configurar os níveis de graves, médios e agudos.

![image](https://github.com/user-attachments/assets/355781d6-c2ce-410f-a104-eb788e85cf68)
*Figura 1: Diagrama de High Level Design mostrando a comunicação entre o app e o serviço.*

#### Componentes do Projeto

1. **EqualizeMe App**:
   - Interface do usuário que permite aos usuários ajustar as configurações de áudio.
   - Conecta-se ao EqualizeMe Service para aplicar as configurações desejadas - por enquanto mostrando mensagem de log com as configurações enviadas.

2. **EqualizeMe Service**:
   - Executa em segundo plano e processa as requisições do EqualizeMe App.
   - Implementa a lógica para ajustar os níveis de áudio usando os métodos definidos na interface AIDL.

3. **Interface AIDL**:
   - Define os métodos `setBass(int value)`, `setMid(int value)` e `setTreble(int value)`.
   - Garante que o aplicativo e o serviço possam comunicar-se de forma eficiente e segura.

#### Métodos AIDL

- `boolean setBass(int value)`: Define o nível de graves se o valor estiver no intervalo de 0 a 10.
- `boolean setMid(int value)`: Define o nível de médio se o valor estiver no intervalo de 0 a 10.
- `boolean setTreble(int value)`: Define o nível de agudos se o valor estiver no intervalo de 0 a 10.

#### Como Compilar e Executar o Projeto no Android Studio

1. **Pré-requisitos**:
   - Instale o Android Studio na sua máquina.
   - Certifique-se de que o SDK do Android está atualizado.

2. **Clonar o Repositório**:
   - Clone o repositório do projeto para o seu ambiente de desenvolvimento local.

3. **Importar o Projeto no Android Studio**:
   - Abra o Android Studio.
   - Selecione "Open an existing Android Studio project" e navegue até o diretório do projeto clonado.

4. **Compilar o Projeto**:
   - No Android Studio, selecione "Build" no menu superior e clique em "Make Project" para compilar o projeto.
   - Resolva quaisquer erros de compilação que possam surgir.

5. **Executar o Projeto**:
   - Conecte um dispositivo Android ou inicie um emulador Android.
   - Clique no botão "Run" (ícone de play) na barra de ferramentas do Android Studio.
   - Selecione o dispositivo ou emulador no qual deseja executar o aplicativo.

6. **Interagir com o App**:
   - Após a execução bem-sucedida, o aplicativo EqualizeMe estará disponível no dispositivo/emulador.
   - Use a interface do usuário para ajustar os níveis de áudio e observe as mudanças aplicadas pelo serviço.

#### Exibição de Imagens de Execução e Logs

- **Captura de Tela da Interface do Aplicativo**:
  ![image](https://github.com/user-attachments/assets/c0cdb4ff-68b5-4811-a4b1-28a3040a2ddf)
  *Figura 2: Interface do usuário do EqualizeMe App para ajustar os níveis de áudio.*

- **Execução de Logs no Android Studio**:
  ![image](https://github.com/user-attachments/assets/30e2b900-0577-4933-b43d-44904c72740b)
  *Figura 3: Exibição dos logs de execução no Android Studio mostrando a conexão entre o aplicativo e o serviço.*

- **Confirmação de Ajustes de Áudio**:
  ![image](https://github.com/user-attachments/assets/b2c7bfce-a649-4046-8826-871a6284912e)
  *Figura 4: Mensagem de confirmação de ajustes aplicados com sucesso.*

#### Considerações Finais

O projeto EqualizeMe é uma demonstração da utilização de serviços Android e comunicação interprocessual usando AIDL. Certifique-se de revisar os logs de execução para depurar e entender o funcionamento interno do aplicativo e do serviço.

Para mais informações, consulte a documentação do Android Developer sobre AIDL e serviços Android: https://developer.android.com/develop/background-work/services/aidl#kotlin
