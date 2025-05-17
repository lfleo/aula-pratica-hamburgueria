package com.example.hamburgueriaz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    // variaveis
    private EditText editTextNome;
    private CheckBox checkBoxBacon;
    private CheckBox checkBoxQueijo;
    private CheckBox checkBoxOnion;
    private Button buttonMenos;
    private Button buttonMais;
    private TextView textViewQuantidade;
    private TextView textViewResumo;
    private TextView textViewPrecoTotal;
    private Button buttonEnviarPedido;

    // variaveis para calculo
    private int quantidade = 1;
    private final double PRECO_BASE = 20.00;
    private final double PRECO_BACON = 2.00;
    private final double PRECO_QUEIJO = 2.00;
    private final double PRECO_ONION = 3.00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicialização dos elementos da interface
        editTextNome = findViewById(R.id.editTextNome);
        checkBoxBacon = findViewById(R.id.checkBoxBacon);
        checkBoxQueijo = findViewById(R.id.checkBoxQueijo);
        checkBoxOnion = findViewById(R.id.checkBoxOnion);
        buttonMenos = findViewById(R.id.buttonMenos);
        buttonMais = findViewById(R.id.buttonMais);
        textViewQuantidade = findViewById(R.id.textViewQuantidade);
        textViewResumo = findViewById(R.id.textViewResumo);
        textViewPrecoTotal = findViewById(R.id.textViewPrecoTotal);
        buttonEnviarPedido = findViewById(R.id.buttonEnviarPedido);

        // listeners para os botões
        buttonMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                somar();
            }
        });

        buttonMenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subtrair();
            }
        });

        buttonEnviarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarPedido();
            }
        });

        // listeners para os checkboxes para atualizar o preço quando marcados/desmarcados
        CheckBox[] checkBoxes = {checkBoxBacon, checkBoxQueijo, checkBoxOnion};
        for (CheckBox cb : checkBoxes) {
            cb.setOnCheckedChangeListener((buttonView, isChecked) -> atualizarPrecoTotal());
        }

        // Atualização inicial do preço
        atualizarPrecoTotal();
    }

     private void somar() {
        quantidade++;
        textViewQuantidade.setText(String.valueOf(quantidade));
        atualizarPrecoTotal();
    }
    private void subtrair() {
        if (quantidade > 1) {
            quantidade--;
            textViewQuantidade.setText(String.valueOf(quantidade));
            atualizarPrecoTotal();
        }
    }
    private double calcularPrecoTotal() {
        double preco = PRECO_BASE;

        if (checkBoxBacon.isChecked()) {
            preco += PRECO_BACON;
        }

        if (checkBoxQueijo.isChecked()) {
            preco += PRECO_QUEIJO;
        }

        if (checkBoxOnion.isChecked()) {
            preco += PRECO_ONION;
        }

        return preco * quantidade;
    }
    private void atualizarPrecoTotal() {
        DecimalFormat formatador = new DecimalFormat("R$ #,##0.00");
        double precoTotal = calcularPrecoTotal();
        textViewPrecoTotal.setText(formatador.format(precoTotal));
    }
    private void enviarPedido() {
        String nome = editTextNome.getText().toString();

        // Verificar se o nome foi preenchido
        if (nome.isEmpty()) {
            Toast.makeText(this, "Por favor, informe seu nome", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criar o resumo do pedido
        String resumo = criarResumoPedido(nome);

        // Exibir o resumo na interface
        textViewResumo.setText(resumo);

        // Enviar o resumo por e-mail usando Intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_SUBJECT, "Pedido de " + nome);
        intent.putExtra(Intent.EXTRA_TEXT, resumo);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nenhum aplicativo de e-mail encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private String criarResumoPedido(String nome) {
        DecimalFormat formatador = new DecimalFormat("R$ #,##0.00");
        double precoTotal = calcularPrecoTotal();

        StringBuilder resumo = new StringBuilder();
        resumo.append(nome).append("\n");
        resumo.append("Tem Bacon? ").append(checkBoxBacon.isChecked() ? "Sim" : "Não").append("\n");
        resumo.append("Tem Queijo? ").append(checkBoxQueijo.isChecked() ? "Sim" : "Não").append("\n");
        resumo.append("Tem Onion Rings? ").append(checkBoxOnion.isChecked() ? "Sim" : "Não").append("\n");
        resumo.append("Quantidade: ").append(quantidade).append("\n");
        resumo.append("Preço final: ").append(formatador.format(precoTotal));

        return resumo.toString();
    }
}
